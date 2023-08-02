package kr.co.tj.bookmark;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookmarkService {

	@Autowired
	private BookmarkRepository bookmarkRepository;

	// 북마크 생성
	public BookmarkDTO createBookmark(BookmarkDTO bookmarkDTO) {
		// TODO Auto-generated method stub

		BookmarkEntity entity = new ModelMapper().map(bookmarkDTO, BookmarkEntity.class);

		Date now = new Date();
		entity.setCreateDate(now);

		entity = bookmarkRepository.save(entity);

		return new ModelMapper().map(entity, BookmarkDTO.class);
	}

	// 북마크 체크
	public BookmarkDTO findByBidAndUsername(Long bid, String username) {
		Optional<BookmarkEntity> optional = bookmarkRepository.findByBidAndUsername(bid, username);
		if (!optional.isPresent()) {
			BookmarkEntity entity = optional.get();
			return new ModelMapper().map(entity, BookmarkDTO.class);
		}
		return null;
	}

	
    // 북마크 삭제
	@Transactional
	public void delete(Long bid) {
		// TODO Auto-generated method stub
		bookmarkRepository.deleteByBid(bid);
	}

	// 내정보 페이지에서 내가 추가한 북마크 확인하기
	public List<BookmarkDTO> findByUsername(String username, int pageNum) {
		// TODO Auto-generated method stub

		List<Sort.Order> sortList = new ArrayList<>();
		sortList.add(Sort.Order.desc("id"));

		Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(sortList));
		Page<BookmarkEntity> pageItem = bookmarkRepository.findByUsername(username, pageable);

		List<BookmarkEntity> list_entity = pageItem.getContent();
		List<BookmarkDTO> list_dto = new ArrayList<>();

		for (BookmarkEntity x : list_entity) {
			BookmarkDTO dto = new ModelMapper().map(x, BookmarkDTO.class);
			list_dto.add(dto);
		}
		return list_dto;
	}

}
