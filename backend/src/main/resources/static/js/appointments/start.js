function startMeasuringWeight() {
    const measuringWeightBox = document.getElementById("measuring-weight-box");
    measuringWeightBox.classList.remove("d-none");
    measuringWeightBox.classList.add("d-block");
}

function inProgressHandle(serviceName) {
    const li = document.createElement("li");
    li.classList.add("d-flex","flex-row", "align-items-center",  "gap-3");

    const div = document.createElement("div");
    const span = document.createElement("span");
    span.innerText = serviceName;
    span.classList.add("fw-bold");

    div.append("Công việc đang thực hiện: ");
    div.append(span);

    const button = document.createElement("button");
    button.innerHTML = '<i class="fa-solid fa-check"></i>';
    button.classList.add("btn", "btn-outline-danger");

    li.append(div, button);

    const task = document.getElementById("task");
    task.classList.remove("d-none");
    task.classList.add("d-block");

    document.getElementById("task-box").innerText = "";
    document.getElementById("task-box").append(li);
}

function convertSecondsToClock(seconds) {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds - (minutes * 60);

    return `${minutes} : ${secs}`;
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
                }
            }
        });

        client.subscribe("/user/queue/timers", (data) => {
            const message = JSON.parse(data.body);
            console.log(message);
            const secondsTillEndAppointment = message.secondsTillEndAppointment;
            const secondsTillEndService = message.secondsTillEndService;

            const startButton = document.getElementById("start");
            startButton.dataset.secs = secondsTillEndAppointment;
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

        setInterval(() => {
            const data = startButton.dataset;
            let secs = parseInt(data.secs, 10);
            if(secs === 0) {
                return;
            }

            startButton.dataset.secs = --secs;

            startButton.innerText = convertSecondsToClock(secs);
        }, 1000);
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