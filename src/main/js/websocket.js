import SockJS from 'sockjs-client';
import 'stompjs';

class StompClient {

	register(registrations) {
		const socket = SockJS('/newseeks');
		const stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			registrations.forEach(function (registration) {
				stompClient.subscribe(registration.route, registration.callback);
			});
		});
	}

}

export default new StompClient();