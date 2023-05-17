import interfaceStore.StoreMenu;

public class Main {
    public static void main(String[] args) throws Exception {

        try (StoreMenu storeMenu = new StoreMenu()) {
            storeMenu.interfaceChoice();
        }
    }
}