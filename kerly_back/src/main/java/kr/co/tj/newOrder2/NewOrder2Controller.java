package kr.co.tj.newOrder2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neworder2-service")
public class NewOrder2Controller {

	@Autowired
	private NewOrder2Service newOrder2Service;

	// 주문 생성
	@PostMapping("/createNewOrder")
	public ResponseEntity<?> createNewOrder(@RequestBody NewOrder2DTO newOrder2DTO) {

		Map<String, Object> map = new HashMap<>();

		if (newOrder2DTO == null) {
			return ResponseEntity.badRequest().body(map);
		}

		try {
			List<NewOrder2DTO> list = newOrder2Service.createNewOrder2(newOrder2DTO);
			map.put("result", list);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", e.getMessage());
			return ResponseEntity.badRequest().body(map);
		}
	}

	// 유저의 주문 내역
	@GetMapping("/newOrderList")
	public ResponseEntity<?> newOrderList(@RequestParam("username") String username,
			@RequestParam("pageNum") int pageNum) {
		Map<String, Object> map = new HashMap<>();

		if (username == null) {
			return ResponseEntity.badRequest().body(map);
		}

		try {
			List<NewOrder2DTO> list = newOrder2Service.newOrderList(username, pageNum);
			map.put("result", list);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", e.getMessage());
			return ResponseEntity.badRequest().body(map);
		}
	}

	// 유저의 주문 상세 내역
	@GetMapping("/newOrderList/id")
	public ResponseEntity<?> newOrderList(@RequestParam("id") Long id, @RequestParam("pageNum") int pageNum) {
		Map<String, Object> map = new HashMap<>();

		if (id == null) {
			return ResponseEntity.badRequest().body(map);
		}

		try {
			List<NewOrder2DTO> list = newOrder2Service.newOrderList(id, pageNum);
			map.put("result", list);
			return ResponseEntity.ok().body(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", e.getMessage());
			return ResponseEntity.badRequest().body(map);
		}
	}

}
