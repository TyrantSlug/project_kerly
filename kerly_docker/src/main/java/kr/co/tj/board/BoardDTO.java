package kr.co.tj.board;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
	
	private Long id;
	
	private String subject;
	
	private String content;
	
	private Date createDate;
	
	private Date updateDate;
	
	private String name;
	
	private String username;
	
	private Long bid;

}
