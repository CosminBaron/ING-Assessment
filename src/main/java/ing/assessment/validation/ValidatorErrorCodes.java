package ing.assessment.validation;

public class ValidatorErrorCodes {
    public static final String CAN_NOT_FOUND_ORDER_WITH_ID = "Can not found order with ID ";
    public static final String ORDER_MUST_CONTAIN_AT_LEAST_ONE_PRODUCT = "Order must contain at least one product.";
    public static final String PRODUCT_QUANTITY_MUST_BE_GREATER_THAN_ZERO = "Product quantity must be greater than zero.";
    public static final String ORDER_COST_MUST_BE_GREATER_THAN_ZERO = "Order cost must be greater than zero.";
    public static final String DELIVERY_TIME_MUST_BE_AT_LEAST_TWO_DAYS = "Delivery time must be at least 2 days.";
    public static final String ORDER_NOT_FOUND = "The order you are trying to manage does not exist. Please check the order ID and try again.";
    public static final String ORDER_CANNOT_BE_CANCELLED = "Orders can only be canceled within the first 2 minutes after placement.";
    public static final String INVALID_DELIVERY_COST = "Invalid delivery cost.";
    public static final String ONE_OR_MORE_PRODUCTS_NOT_FOUND_IN_DATABASE = "One or more products not found in database.";
    public static final String PRODUCT_PRICE_MUST_BE_GREATER_THAN_ZERO = "Product price must be greater than 0";
    public static final String CAN_NOT_FOUND_PRODUCT_WITH_ID = "Can not found product with ID ";
    public static final String INSUFFICIENT_STOCK_FOR_PRODUCT_ID = "Insufficient stock for product ID ";
    public static final String INVALID_ORDER = "Invalid order";
    public static final String DUPLICATE_PRODUCT_ID_FOUND = "Duplicate productId found: ";
    public static final String PRODUCT_SHOULD_HAVE_ID_AND_QUANTITY = "Product should have id and quantity";




}
