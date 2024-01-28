package wang.ulane.freeswitch;

import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEslInboundTest {
	private static final Logger log = LoggerFactory.getLogger(EventEslInboundTest.class);
	private static String host = "127.0.0.1";
	private static int port = 8021;
	private static String password = "ClueCon";

	public static void inBand() {
		final Client client = new Client();
		try {
			client.connect(host, port, password, 10);
			// client.connect( "10.0.0.85", port, password, 10 );
		} catch (InboundConnectionFailure e) {
			log.error("Connect failed", e);
			return;
		}
		// 注册事件处理程序
		client.addEventListener(new IEslEventListener() {
			public void eventReceived(EslEvent event) {
				System.out.println("Event received [{}]" + event.getEventHeaders());
				// 记录接听次数和时间
				if (event.getEventName().equals("CHANNEL_ANSWER")) {
					// your code here
				}
				if (event.getEventName().equals("HEARTBEAT")) {
					System.out.println("recieved Hearbeat event !" + event.getEventBodyLines());
				}
				if (event.getEventName().equals("CHANNEL_DESTROY")) {
				}
				if (event.getEventName().equals("CHANNEL_HANGUP_COMPLETE")) {
					// 挂断
				}
			}

			public void backgroundJobResultReceived(EslEvent event) {
				String uuid = event.getEventHeaders().get("Job-UUID");
				log.info("Background job result received+:" + event.getEventName() + "/" + event.getEventHeaders());// +"/"+JoinString(event.getEventHeaders())+"/"+JoinString(event.getEventBodyLines()));
			}
		});
		// 定义事件日志输出格式,但是java esl 目前只支持plain格式
		// ，http://wiki.freeswitch.org/wiki/Event_Socket
		// 2012-12-25 19:20:30 [ main:426 ] - [ ERROR ]
		// java.lang.IllegalStateException: Only 'plain' event format is
		// supported at present
		client.setEventSubscriptions("plain", "all");
		// client.close();
	}

	public static void main(String[] args) {
		inBand();
		System.out.println("event_socket_test".toUpperCase());
	}
}
