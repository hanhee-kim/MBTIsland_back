package com.kosta.mbtisland.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;
//	private static final String ADMIN_ADDRESS="MBTIsland@naver.com";
	private String ePw; //인증번호
	
	//mail작성
	public MimeMessage createMail(String to) throws MessagingException, UnsupportedEncodingException {
		System.out.println("보내는 대상 : " + to);
		System.out.println("인증 번호 : " + ePw);
		
		MimeMessage message = mailSender.createMimeMessage();

		message.addRecipients(Message.RecipientType.TO, to);// 보내는 대상
		message.setSubject("MBTIsland 회원가입 이메일 인증");// 제목

		String msgg = "";
		msgg += "<div style={{background-color:'#fdfdfd',border-radius:'15px',box-shadow:'5px 5px 5px 5px gray '}}>";
		msgg += "<div style={{margin:'100px',text-align:'center'}}>";
		msgg += "<h1> * 안녕하세요 * </h1>";
		msgg += "<br/>";
		msgg += "<h1> MBTI 커뮤니티 MBTIsland 입니다.</h1>";
		msgg += "<br/>";
		msgg += "<div style={{font-size:'130%'}}>아래 코드를 회원가입 창으로 돌아가 입력해주세요</div>";
		msgg += "<br/>";
		msgg += "<div style={{fontSize:'130%'}}>저희 MBTIsland를 찾아주셔서 감사합니다!</div>";
		msgg += "<br/>";
		msgg += "<div style={{border:'1px solid black',border-radius:'20px'}}>";
		msgg += "<br/>";
		msgg += "<h3 style={{color:'black'}}>회원가입 인증 코드입니다.</h3>";
		msgg += "<div style={{fontSize:'130%',letterSpacing:'3px'}}>";
		msgg += "CODE:<strong>";
		msgg += ePw + "</strong><div><br/> "; // 메일에 인증번호 넣기
		msgg += "</div>";
		
		
		
//	      
//	        
//	          
//	          <br/>
//	            
//	          <br/>
//	            
//	          <br/>
//	              
//	                <br/>
//	                
//	                
//	                
//	              </div>
//	        <br/> 
//	      </div>
//	    </div>
//	      </div>
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
		// 보내는 사람의 이메일 주소, 보내는 사람 이름
		message.setFrom(new InternetAddress("914gksl914@naver.com", "MBTIsland_Admin"));// 보내는 사람
		return message;
	}
	
	//랜덤 인증 코드 발송
	public String createKey() {
		StringBuffer key = new StringBuffer();
		Random random = new Random();

		for (int i = 0; i < 8; i++) { // 인증코드 8자리
			int index = random.nextInt(3); // 0~2 까지 랜덤, random 값에 따라서 아래 switch 문이 실행됨

			switch (index) {
			case 0:
				key.append((char) ((int) (random.nextInt(26)) + 97));
				// a~z (ex. 1+97=98 => (char)98 = 'b')
				break;
			case 1:
				key.append((char) ((int) (random.nextInt(26)) + 65));
				// A~Z
				break;
			case 2:
				key.append((random.nextInt(10)));
				// 0~9
				break;
			}
		}

		return key.toString();
	}

		// 메일 발송
		// sendSimpleMessage 의 매개변수로 들어온 to 는 곧 이메일 주소가 되고,
		// MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다.
		// 그리고 bean 으로 등록해둔 javaMail 객체를 사용해서 이메일 send!!		
		public String sendSimpleMessage(String to) throws Exception {

			ePw = createKey(); // 랜덤 인증번호 생성

			MimeMessage message = createMail(to); // 메일 발송
			try {// 예외처리
				mailSender.send(message);
			} catch (MailException es) {
				es.printStackTrace();
				throw new IllegalArgumentException();
			}
			return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
		}
	

	
}
