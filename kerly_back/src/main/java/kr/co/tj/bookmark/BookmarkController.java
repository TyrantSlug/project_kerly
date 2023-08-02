package kr.co.tj.bookmark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookmark-service")
public class BookmarkController {

	@Autowired
	private BookmarkService bookmarkService;

	// 북마크 생성
	@PostMapping("/createBookmark")
	public ResponseEntity<?> createBookmark(@RequestBody BookmarkDTO bookmarkDTO) {
		Map<String, Object> map = new HashMap<>();

		if (bookmarkDTO.getUsername() == null 
				|| bookmarkDTO.getItemName() == null
				|| bookmarkDTO.getSellerName() == null) {
			return ResponseEntity.badRequest().body(map);
		}

		try {
			bookmarkDTO = bookmarkService.createBookmark(bookmarkDTO);
			map.put("result", bookmarkDTO);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "등록 실패");
			return ResponseEntity.badRequest().body(map);
		}
	}

	// 북마크 체크
	@GetMapping("/bid/{bid}/username/{username}")
	public ResponseEntity<?> findByBidAndUsername(@PathVariable Long bid, @PathVariable String username) {
		Map<String, Object> map = new HashMap<>();

		if (bid == null 
				|| username == null) {
			return ResponseEntity.badRequest().body(map);
		}

		try {
			BookmarkDTO bookmarkDTO = bookmarkService.findByBidAndUsername(bid, username);
			map.put("result", bookmarkDTO);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "실패");
			return ResponseEntity.badRequest().body(map);
		}
	}

	// 개인정보 페이지에서 자신의 북마크 확인하기
	@GetMapping("/username")
	public ResponseEntity<?> findByUsername(@RequestParam("username") String username,
			@RequestParam("pageNum") int pageNum) {

		if (username == null) {
			return ResponseEntity.badRequest().body("아이디가 없습니다.");
		}

		try {
			List<BookmarkDTO> list = bookmarkService.findByUsername(username, pageNum);
			return ResponseEntity.ok().body(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e);
		}
	}

	// 북마크 삭제
	@DeleteMapping("deleteBookmark")
	public ResponseEntity<?> deleteBookmark(@RequestBody BookmarkDTO bookmarkDTO) {
		Map<String, Object> map = new HashMap<>();
		if (bookmarkDTO == null) {
			return ResponseEntity.badRequest().body(map);
		}
		try {
			bookmarkService.delete(bookmarkDTO.getBid());
			map.put("result", bookmarkDTO);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "실패");
			return ResponseEntity.badRequest().body(map);
		}
	}

}
