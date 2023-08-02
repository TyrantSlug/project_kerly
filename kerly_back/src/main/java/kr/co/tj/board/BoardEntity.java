package kr.co.tj.board;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 200, nullable = false) // 값이 존재해야 함.
	private String subject;
	
	@Column(nullable = false)
	private String content;
	
	private Date createDate;
	
	private Date updateDate;
	
	@Column(nullable=false)
	private String name;
	
	private String username;
	

}
