window.addEventListener('DOMContentLoaded', event => {
    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/paws-ws'
    });

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/greetings', (msg) => {
            console.log(msg.body);
        });
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };

    stompClient.activate();

    setTimeout(() => {
        stompClient.publish({
            destination: "/app/hello",
            body: JSON.stringify({name : "hehahds"})
        });
    }, 1000);
});

