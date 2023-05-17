package products;

import lombok.Getter;

@Getter
public class Category {
    Integer id_category;
    String category;

    public Category(Integer id_category, String category) {
        this.id_category = id_category;
        this.category = category;
    }


    @Override
    public String toString() {
        return
                "id: " + id_category + " - " +
                  category;
    }
}
