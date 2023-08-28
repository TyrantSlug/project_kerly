package kr.co.tj.newOrder;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewOrderDTO {

    private Long id;
    private Long totalPrice;
    private Date createDate;
    private String username;
    private List<OrderItemDTO> orderItems;
    private String orderNum;

}