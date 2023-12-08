let currentTaskNum = 0;
let overallInterval;
let serviceInterval;

function startOverallInterval() {
    const startButton = document.getElementById("start");

    overallInterval = setInterval(() => {
        const data = startButton.dataset;
        let secs = parseInt(data.secs, 10);
        if(secs === -1) {
            return;
        }

        startButton.innerText = convertSecondsToClock(secs);

        startButton.dataset.secs = --secs;
    }, 950);
}

function startServiceInterval() {
    const taskCard = document.getElementById(`task-${currentTaskNum}`);
    const cardFooter = taskCard.querySelector(".card-footer");

    serviceInterval = setInterval(() => {
        let secs = parseInt(cardFooter.dataset.secs, 10);
        if(secs === -1) {
            return;
        }

        cardFooter.innerText = convertSecondsToClock(secs);

        cardFooter.dataset.secs = --secs;
    }, 950);
}

function startMeasuringWeight() {
    const measuringWeightBox = document.getElementById("measuring-weight-box");
    measuringWeightBox.classList.remove("d-none");
    measuringWeightBox.classList.add("d-block");
}

function inProgressHandle(service) {
    currentTaskNum++;
    const card = document.createElement("div");
    card.classList.add("card", "text-center");
    card.id = `task-${currentTaskNum}`;

    const cardHeader = document.createElement("div");
    cardHeader.classList.add("card-header", "fw-bold");
    cardHeader.innerHTML = `#${currentTaskNum}. ${service.name} <span class="badge text-bg-danger">Chưa hoàn thành</span>`;

    const cardBody = document.createElement("div");
    cardBody.classList.add("card-body");
    cardBody.innerHTML += `<h5 class="card-title">${service.name}</h5>`;
    cardBody.innerHTML += `<p class="card-text">${service.description}</p>`;
    cardBody.innerHTML += `<button class="btn btn-primary">Hướng dẫn</button>`;

    const cardFooter = document.createElement("div");
    cardFooter.classList.add("card-footer", "text-muted");
    cardFooter.innerText = "00 : 00";
    card.append(cardHeader, cardBody, cardFooter);

    const task = document.getElementById("task");
    task.classList.remove("d-none");
    task.classList.add("d-block");

    document.getElementById("task-box").append(card);
    document.getElementById("task-box").append(document.createElement("br"));

    cardFooter.dataset.secs = 0;
    startServiceInterval();
}

function taskCompletedHandle() {
    const taskCard = document.getElementById(`task-${currentTaskNum}`);
    const badge = taskCard.querySelector(".badge");
    badge.classList.remove("text-bg-danger");
    badge.classList.add("text-bg-success");
    badge.innerText = "Đã hoàn thành";
}

function doneHandle() {
    const button = document.getElementById("start");
    button.innerText = "Đã hoàn thành";
}

function convertSecondsToClock(seconds) {
    const minutes = Math.floor(seconds / 60).toLocaleString("en-US", {
        minimumIntegerDigits: 2,
        useGrouping: false
    });
    const secs = (seconds - (minutes * 60)).toLocaleString("en-US", {
        minimumIntegerDigits: 2,
        useGrouping: false
    });

    return `${minutes} : ${secs}`;
}

function paymentHandle(url, client) {
    const payment = document.getElementById("payment");
    payment.classList.remove("d-none");
    payment.classList.add("d-block");

    const urlSpan = document.createElement("span");
    urlSpan.innerText = url;

    const qrBox = document.getElementById("qr-box");
    qrBox.append(urlSpan);

    new QRCode(document.getElementById("qr-code"), {
        text: url,
        width: 170,
        height: 170,
        colorDark : "#000000",
        colorLight : "#ffffff",
        correctLevel : QRCode.CorrectLevel.H
    });

    const button = document.getElementById("paid-in-cash");
    button.onclick = () => {
        client.publish({
            destination: '/app/paid',
            body: appointmentId
        });
    };
}

function initStompClient() {
    const client = new StompJs.Client();
    client.brokerURL = 'ws://localhost:8080/paws-ws';

    client.onConnect = function (frame) {
        console.log("Connected to server", frame);

        setTimeout(() => {
            client.publish({
                destination: '/app/start-appointment',
                body: JSON.stringify({appointmentItemId, appointmentId})
            });
        }, 1000);

        client.subscribe("/user/queue/appointments", (data) => {
            const pl = JSON.parse(data.body);
            console.log(pl);

            switch(pl.message) {
                case "start_measuring_weight": {
                    startMeasuringWeight();
                    break;
                }

                case "in_progress": {
                    inProgressHandle(pl.payload);
                    break;
                }

                case "task_completed": {
                    taskCompletedHandle();
                    break;
                }

                case "payment": {
                    paymentHandle(pl.payload, client);
                    break;
                }

                case "done": {
                    doneHandle();
                    client.deactivate();
                }
            }
        });

        client.subscribe("/user/queue/timers", (data) => {
            const message = JSON.parse(data.body);
            console.log(message);
            const {secondsTillEndAppointment, secondsTillEndService} = message;

            const startButton = document.getElementById("start");
            startButton.dataset.secs = secondsTillEndAppointment;

            if(typeof overallInterval !== "undefined") {
                clearInterval(overallInterval);
                startOverallInterval();
            }

            const cardFooter = document.querySelector(`#task-${currentTaskNum} .card-footer`);
            cardFooter.dataset.secs = secondsTillEndService;

            if(typeof serviceInterval !== "undefined") {
                clearInterval(serviceInterval);
                startServiceInterval();
            }
        });
    };

    client.onStompError = function (frame) {
        console.log('Broker reported error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
    };

    return client;
}

window.addEventListener("DOMContentLoaded", () => {
    const client = initStompClient();

    const startButton = document.getElementById("start");
    startButton.addEventListener("click", function() {
        client.activate();
        this.disabled = true;
        startButton.dataset.secs = 0;

        startOverallInterval();
    });

    if(isProcessing) {
        startButton.click();
    }

    const measuringWeightForm = document.getElementById("measuring-weight-form");
    measuringWeightForm.addEventListener("submit", function (event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const weight = formData.get("weight");

        client.publish({
            destination: '/app/measure-weight',
            body: JSON.stringify({appointmentItemId, appointmentId, weight})
        });

        event.target.disabled = true;
        event.submitter.disabled = true;
    });
});