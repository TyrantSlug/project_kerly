package kr.co.tj.newOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import kr.co.tj.item.ItemEntity;
import kr.co.tj.item.ItemService;

import java.util.Random;

@Service
@ComponentScan("kr.co.tj.item")
public class NewOrderService {

    @Autowired
    private NewOrderRepository newOrder2Repository;
    
    @Autowired
	private ItemService itemService;
    
    


    public List<NewOrderDTO> createNewOrder2(NewOrderDTO newOrder2DTO) {
        Date currentDate = new Date();
        newOrder2DTO.setCreateDate(currentDate);

        NewOrderEntity newOrder2Entity = new ModelMapper().map(newOrder2DTO, NewOrderEntity.class);

        // 주문 번호 생성 및 중복 확인
        String orderNum = generateUniqueOrderNumber();
        newOrder2Entity.setOrderNum(orderNum);

        // OrderItemDTO 리스트를 OrderItemEntity 리스트로 변환하여 설정
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemDTO orderItemDTO : newOrder2DTO.getOrderItems()) {
            OrderItemEntity orderItemEntity = new ModelMapper().map(orderItemDTO, OrderItemEntity.class);
            orderItems.add(orderItemEntity);

            ItemEntity itemEntity = ItemEntity.builder()
                    .id(orderItemEntity.getItemId())
                    .ea(orderItemEntity.getItemStock())
                    .build();

            String result = itemService.updateEaByProductId(itemEntity);

        }

        // totalPrice 계산
        Long totalPrice = calculateTotalPrice(orderItems);
        newOrder2Entity.setTotalPrice(totalPrice);

        newOrder2Repository.save(newOrder2Entity);

        // 저장된 엔티티를 다시 DTO로 변환하여 반환
        List<NewOrderDTO> savedOrderDTOs = new ArrayList<>();
        savedOrderDTOs.add(new ModelMapper().map(newOrder2Entity, NewOrderDTO.class));
        return savedOrderDTOs;
    }

    private Long calculateTotalPrice(List<OrderItemEntity> orderItems) {
        Long totalPrice = 0L;
        for (OrderItemEntity orderItemEntity : orderItems) {
            totalPrice += orderItemEntity.getItemTotalPrice();
        }
        return totalPrice;
    }

    private String generateUniqueOrderNumber() {
        // 8자리의 랜덤 숫자 생성
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        String orderNum = sb.toString();

        // DB에서 주문 번호 중복 확인
        while (isOrderNumberExists(orderNum)) {
            orderNum = generateUniqueOrderNumber(); // 중복된 경우 재귀 호출로 다시 생성
        }

        return orderNum;
    }

    private boolean isOrderNumberExists(String orderNum) {
        // order_num 필드를 가진 NewOrder2Entity를 DB에서 조회하여 주문 번호의 중복 여부 확인
        // 이미 존재하면 true, 그렇지 않으면 false를 반환
        return false;
    }

    
	public List<NewOrderDTO> newOrderList(String username, int pageNum) {
		List<Sort.Order> sortList = new ArrayList<>();
	       sortList.add(Sort.Order.desc("id"));
	       
	       Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(sortList));
	       Page<NewOrderEntity> pageItem = newOrder2Repository.findByUsername(username, pageable);
		
	       
	       List<NewOrderEntity> list_entity = pageItem.getContent();
	       List<NewOrderDTO> list_dto = new ArrayList<>();
	       
	       for(NewOrderEntity x : list_entity) {
	    	   NewOrderDTO dto = new ModelMapper().map(x, NewOrderDTO.class);
	    	   list_dto.add(dto);
	       }
		return list_dto;
	}

	public List<NewOrderDTO> newOrderList(Long id, int pageNum) {
		// TODO Auto-generated method stub
		List<Sort.Order> sortList = new ArrayList<>();
	       sortList.add(Sort.Order.desc("id"));
	       
	       Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(sortList));
	       Page<NewOrderEntity> pageItem = newOrder2Repository.findById(id, pageable);
		
	       
	       List<NewOrderEntity> list_entity = pageItem.getContent();
	       List<NewOrderDTO> list_dto = new ArrayList<>();
	       
	       for(NewOrderEntity x : list_entity) {
	    	   NewOrderDTO dto = new ModelMapper().map(x, NewOrderDTO.class);
	    	   list_dto.add(dto);
	       }
		return list_dto;
	}
}