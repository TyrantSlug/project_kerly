package kr.co.tj.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("board-service")
public class BoardController {

   @Autowired
   private BoardService boardService;
   

   @PostMapping("/write")
   public ResponseEntity<?> save(@RequestBody BoardDTO boardDTO) {

      Map<String, Object> map = new HashMap<>();

      if (boardDTO.getSubject() == null || boardDTO.getContent().isEmpty()) {
         return ResponseEntity.badRequest().body("제목이 공백입니다");
      }

      if (boardDTO.getContent() == null || boardDTO.getContent().isEmpty()) {
         return ResponseEntity.badRequest().body("내용이 공백입니다");
      }

      try {
         boardDTO = boardService.save(boardDTO);
         map.put("result", boardDTO);
         return ResponseEntity.ok().body(map);
      } catch (Exception e) {
         e.printStackTrace();
         map.put("err", e.getMessage());
         return ResponseEntity.badRequest().body(map);
      }
   }

   @GetMapping("/all")
   public ResponseEntity<?> findAll() {

      Map<String, Object> map = new HashMap<>();

   
      try {
         List<BoardDTO> dtoList = boardService.findAll();
         map.put("result", dtoList);
         return ResponseEntity.ok().body(map);
      } catch (Exception e) {
         e.printStackTrace();
         map.put("err", e.getMessage());
         return ResponseEntity.badRequest().body(map);
      }

   }
   
   @GetMapping("/id/{id}")
   public ResponseEntity<?> findById(@PathVariable("id") Long id){
      
      Map<String, Object> map = new HashMap<>();
      
      if(id == null || id == 0L) {
         return ResponseEntity.badRequest().body("아이디가 없습니다.");
      }
      
      BoardDTO dto;
      
      try {
         dto = boardService.findById(id);
         map.put("result", dto);
         return ResponseEntity.ok().body(map);
      } catch (Exception e) {
         e.printStackTrace();
         map.put("err", e.getMessage());
         return ResponseEntity.badRequest().body(map);
      }
   }
}