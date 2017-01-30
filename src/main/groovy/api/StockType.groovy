package api

enum StockType {
    REMOTE(0), DEMAND(1), STOCK(2)

    int code

    StockType(int code){
        this.code = code
    }
}