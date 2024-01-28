package wang.ulane.freeswitch;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;

public class InboundApp {

	public static void main(String[] args) throws InterruptedException {
		Client client = new Client();
		try {
			// 连接freeswitch
			client.connect("localhost", 8021, "ClueCon", 10);

			client.addEventListener(new IEslEventListener() {

				@Override
				public void eventReceived(EslEvent event) {
					String eventName = event.getEventName();
					// 这里仅演示了CHANNEL_开头的几个常用事件
					if (eventName.startsWith("CHANNEL_")) {
						String calleeNumber = event.getEventHeaders().get("Caller-Callee-ID-Number");
						String callerNumber = event.getEventHeaders().get("Caller-Caller-ID-Number");
						switch (eventName) {
							case "CHANNEL_CREATE":
								System.out.println("发起呼叫, 主叫：" + callerNumber + " , 被叫：" + calleeNumber);
								break;
							case "CHANNEL_BRIDGE":
								System.out.println("用户转接, 主叫：" + callerNumber + " , 被叫：" + calleeNumber);
								break;
							case "CHANNEL_ANSWER":
								System.out.println("用户应答, 主叫：" + callerNumber + " , 被叫：" + calleeNumber);
								break;
							case "CHANNEL_HANGUP":
								String response = event.getEventHeaders().get("variable_current_application_response");
								String hangupCause = event.getEventHeaders().get("Hangup-Cause");
								System.out.println("用户挂断, 主叫：" + callerNumber + " , 被叫：" + calleeNumber + " , response:"
										+ response + " ,hangup cause:" + hangupCause);
								break;
							case "CHANNEL_HANGUP_COMPLETE":
								Map<String, String> map = event.getEventHeaders();
								for(Entry<String, String> ent:map.entrySet()){
									System.out.println("headers:"+ent.getKey()+":"+ent.getValue());
								}
								break;
							default:
								System.out.println("其他事件："+eventName);
//								Map<String, String> map = event.getEventHeaders();
//								for(Entry<String, String> ent:map.entrySet()){
//									System.out.println("headers:"+ent.getKey()+":"+ent.getValue());
//								}
								break;
						}
					}
				}

				@Override
				public void backgroundJobResultReceived(EslEvent event) {
					String jobUuid = event.getEventHeaders().get("Job-UUID");
					System.out.println("异步回调:" + jobUuid);
					System.out.println(event.getEventName());
					List<String> bodyLines = event.getEventBodyLines();
					for (String string : bodyLines) {
						System.out.println(string);
					}
				}
			});

			client.setEventSubscriptions("plain", "all");

			// 这里必须检查，防止网络抖动时，连接断开
			if (client.canSend()) {
				System.out.println("连接成功，准备发起呼叫...");
				// （异步）向1000用户发起呼叫，用户接通后，播放音乐/tmp/demo1.wav
//				String callResult = client.sendAsyncApiCommand("originate", "user/1003 &playback(/tmp/demo.wav)");
				String callResult = client.sendAsyncApiCommand("originate", "user/1003 &playback(ivr/ivr-that_was_an_invalid_entry.wav)");
				System.out.println("api uuid:" + callResult);
			}

		} catch (InboundConnectionFailure inboundConnectionFailure) {
			System.out.println("连接失败！");
			inboundConnectionFailure.printStackTrace();
		}

	}
}