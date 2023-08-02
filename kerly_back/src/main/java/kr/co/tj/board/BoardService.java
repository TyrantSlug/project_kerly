package kr.co.tj.board;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

   @Autowired
   private BoardRepository boardRepository;

   public BoardDTO save(BoardDTO boardDTO) {

      BoardEntity boardEntity = new BoardEntity();

      boardEntity.setSubject(boardDTO.getSubject());
      boardEntity.setContent(boardDTO.getContent());
      boardEntity.setCreateDate(new Date());
      boardEntity.setUpdateDate(new Date());
      boardEntity.setName(boardDTO.getName());

      boardEntity = boardRepository.save(boardEntity);

      return new ModelMapper().map(boardEntity, BoardDTO.class);
   }

   public List<BoardDTO> findAll() {
      List<BoardEntity> boardEntity = boardRepository.findAll();
      List<BoardDTO> boardDTO = new ArrayList<>();

      for (BoardEntity e : boardEntity) {
         boardDTO.add(new ModelMapper().map(e, BoardDTO.class));
         ;
      }

      return boardDTO;
   }

   public BoardDTO findById(Long id) {

      Optional<BoardEntity> optional = boardRepository.findById(id);

      if (!optional.isPresent()) {
         throw new RuntimeException("잘못된 접근입니다. 댓글이 존재하지 않습니다.");
      }

      BoardEntity boardEntity = optional.get();

      BoardDTO boardDTO = new ModelMapper().map(boardEntity, BoardDTO.class);

      return boardDTO;
   }

   

}