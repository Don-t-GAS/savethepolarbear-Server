package donot.gas.back.dto;

import donot.gas.back.entity.Order;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.swing.text.html.parser.Entity;

@Data
@RequiredArgsConstructor
public class OrderDto {

    private final String company;
    private final String kinds;
    private final String model;
    private final Integer grade;
    private final Integer orderCount;

    public OrderDto(Order entity) {
        this.company = entity.getCompany();
        this.kinds = entity.getKinds();
        this.model = entity.getModel();
        this.grade = entity.getGrade();
        this.orderCount = entity.getOrderCount();
    }
}
