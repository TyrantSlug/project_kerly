package kr.co.tj.file;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/file-service")
public class FileController {

	@Autowired
	private FileService fileService;

	// 슬라이더 이미지 가져오기
	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		Map<String, Object> map = new HashMap<>();

		try {
			List<UrlDTO> urlList = fileService.findAll();
			map.put("result", urlList);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "이미지 리스트를 불러오지 못했습니다");
			return ResponseEntity.ok().body(map);
		}
	}

	// ?? url 가져오는 코드인데 사용처 존재 유무 확인 필요
	@GetMapping("/id/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<>();

		if (id == null) {
			map.put("result", "잘못된 접근입니다. 존재하지 않는 id입니다.");
			return ResponseEntity.badRequest().body(map);
		}

		try {
			UrlDTO urlDTO = fileService.findById(id);
			map.put("result", urlDTO);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {

			e.printStackTrace();
			map.put("result", e.getMessage());
			return ResponseEntity.badRequest().body(map);
		}
	}

	// 슬라이드 이미지 주소 삭제
	@DeleteMapping("/urlDelete")
	public ResponseEntity<?> urlDelete(@RequestBody UrlDTO urlDTO) {
		Map<String, Object> map = new HashMap<>();

		if (urlDTO == null || urlDTO.getId() == null || urlDTO.getId() == 0L) {
			return ResponseEntity.badRequest().body(map);
		}

		try {
			fileService.urlDelete(urlDTO.getId());
			map.put("result", "삭제 성공");
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "삭제 실패");
			return ResponseEntity.badRequest().body(map);
		}
	}
	
	
	// 슬라이드 이미지 주소 업로드
	@PostMapping("/urlUpload")
	public ResponseEntity<?> urlUpload(@RequestBody UrlDTO urlDTO) {
		Map<String, Object> map = new HashMap<>();

		if (urlDTO.getUsername() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자가 존재하지 않습니다.");
		}

		if (urlDTO.getImageUrl() == null) {
			map.put("result", "url이 없습니다");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		}

		try {
			urlDTO = fileService.urlUpload(urlDTO);
			map.put("result", urlDTO);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "등록 실패");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		}
	}

	
	// 이미지 파일 업로드
	@PostMapping("/fileUpload")
	public ResponseEntity<?> fileUpload(MultipartHttpServletRequest mRequest) {
		Map<String, Object> map = new HashMap<>();
		MultipartFile file = mRequest.getFile("file");
		String originalName = file.getOriginalFilename();
		String savedName = FileService.makeFilename(originalName);
		Date date = new Date();
		String uploaderId = mRequest.getParameter("uploaderId");
		String bid = mRequest.getParameter("bid");
		Long bid2 = Long.parseLong(bid);
		String itemType = mRequest.getParameter("itemType");
		String itemName = mRequest.getParameter("itemName");
		try {
			FileDTO dto = new FileDTO();
			dto.setBid(bid2);
			dto.setOriginalName(originalName);
			dto.setSavedName(savedName);
			dto.setUploadDate(date);
			dto.setUploaderId(uploaderId);
			dto.setItemName(itemName);
			dto.setItemType(itemType);
			byte[] bytes = file.getBytes();
			FileEntity fileEntity = fileService.uploadFile(bytes, dto);
			bytes = Base64.encodeBase64(bytes);
			String strBytes = new String(bytes, "UTF-8");
			map.put("bytes", strBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("result", "ok");
		return ResponseEntity.ok().body(map);
	}

	// 이미지 가져오기
	@GetMapping("fileDownload/{bid}")
	public ResponseEntity<?> fileDownload(@PathVariable("bid") Long bid) {
		byte[] bytes = fileService.fintByBid(bid);
		if (bytes != null) {
			ByteArrayResource resource = new ByteArrayResource(bytes);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			return ResponseEntity.ok().headers(headers).contentLength(bytes.length).body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	//이미지 삭제
	@DeleteMapping("fileDelete")
	public ResponseEntity<?> filedelete(@RequestBody FileDTO fileDTO) {
		Map<String, Object> map = new HashMap<>();

		// 조건 작성하면 작동하지 않음. 존재 유무로 삭제를 하게 해뒀음.


		fileService.delete(fileDTO.getBid());
		map.put("result", fileDTO);
		return ResponseEntity.ok().body(map);

	}

}
