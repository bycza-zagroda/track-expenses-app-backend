package pl.byczazagroda.trackexpensesappbackend;

import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.auth.userModel.User;
import pl.byczazagroda.trackexpensesappbackend.auth.userModel.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class TestUtils {

    public static final Long ID_1L = 1L;

    public static final Long USER_ID_1L = 1L;

    public static final Long WALLET_ID_1L = 1L;

    public static final Long FINANCIAL_TRANSACTION_ID_1L = 1L;

    public static final Long FINANCIAL_TRANSACTION_CATEGORY_ID_1L = 1L;
    public static final String EXAMPLE_CATEGORY_NAME = "Example category name_";
    public static final FinancialTransactionType EXAMPLE_FINANCIAL_TRANSACTION_TYPE = FinancialTransactionType.INCOME;
    public static final Instant EXAMPLE_CATEGORY_CREATION_DATE = Instant.now();

    private TestUtils() {
    }

    /**
     * Create one test {@link User} with specific properties:
     * <ul>
     * <li>{@link Long id} with value 1L</li>
     * <li>{@link String name}</li>
     * <li>{@link String email}</li>
     * <li>{@link String password}</li>
     * <li>{@link UserStatus status} with value {@link UserStatus#VERIFIED VERIFIED}</li>
     * </ul>
     *
     * @return {@code User}
     */
    public static User createUserForTest() {
        return createUserForTest(1L);
    }

    /**
     * Create one test {@link User} with specific properties:
     * <ul>
     * <li>{@link String name}</li>
     * <li>{@link String email}</li>
     * <li>{@link String password}</li>
     * <li>{@link UserStatus status} with value {@link UserStatus#VERIFIED VERIFIED}</li>
     * </ul>
     *
     * @param userId - user Id
     * @return {@code User}
     */
    public static User createUserForTest(Long userId) {
        return User.builder()
                .id(userId)
                .userName("UserOne")
                .email("user@server.domain.com")
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
    }

    /**
     * Create one test {@link User} with specific properties:
     * <ul>
     * <li>{@link Long id} - the value is taken from the method parameter</li>
     * <li>{@link String name}</li>
     * <li>{@link String email}</li> - the value is taken from the method parameter</li>
     * <li>{@link String password}</li>
     * <li>{@link UserStatus status} with value {@link UserStatus#VERIFIED VERIFIED}</li>
     * </ul>
     *
     * @param email {@code String}
     * @return {@code User}
     */
    public static User createUserWithEmailForTest(Long userId, String email) {
        final User user = createUserForTest(userId);
        user.setEmail(email);

        return user;
    }

    /**
     * Create test users list {@link User} with specific properties:
     * <ul>
     * <li>{@link String name}</li>
     * <li>{@link String email}</li>
     * <li>{@link String password}</li>
     * <li>{@link UserStatus status}</li>
     * </ul>
     * <p>
     * All created users on the list got same properties [name, email, password, status]. Properties <b>id</b> are different.
     *
     * @param count number of elements to create
     * @return {@code User} list
     */
    public static List<User> createTestUserList(int count) {
        List<User> list = new ArrayList<>(count);
        for (long i = 1L; i <= count; i++) {
            list.add(createUserForTest(i));
        }

        return list;
    }


    /**
     * Create one test {@link Wallet} with specific properties:
     * <ul>
     * <li>{@link Long id} with value 1L</li>
     * <li>{@link User user}</li>
     * <li>{@link Instant creation date}</li>
     * <li>{@link String name}</li>
     * </ul>
     *
     * @param user {@code User}
     * @return {@code Wallet}
     */
    public static Wallet createWalletForTest(User user) {
        return Wallet.builder()
                .id(1L)
                .user(user)
                .creationDate(Instant.now())
                .name("Example wallet name")
                .build();
    }


    /**
     * Create {@link FinancialTransactionCategory Financial Transactions Category} list with specific properties:
     * <ul>
     * <li>{@link String Financial Transactions Category name}</li>
     * <li>{@link FinancialTransactionType Financial Transaction Type}</li>
     * <li>{@link Instant creation date}</li>
     * </ul>
     * The method does not set the <b>id</b> property - the value is set by the database
     *
     * @param count number of elements to create
     * @return {@code FinancialTransactionCategory} list
     */
    public static List<FinancialTransactionCategory> createFinancialTransactionCategoryListForTest(
            int count,
            FinancialTransactionType type,
            User user) {
        final String name = EXAMPLE_CATEGORY_NAME;

        ArrayList<FinancialTransactionCategory> list = new ArrayList<>(count);
        for (long i = 1; i <= count; i++) {
            list.add(FinancialTransactionCategory.builder()
                    .id(i)
                    .name(name + i)
                    .type(type)
                    .creationDate(EXAMPLE_CATEGORY_CREATION_DATE)
                    .user(user)
                    .build()
            );
        }

        return list;
    }

    /**
     * Create {@link FinancialTransactionCategoryDTO Financial Transactions Category DTO} list with specific properties:
     * <ul>
     * <li>{@link String Financial Transactions Category name}</li>
     * <li>{@link FinancialTransactionType Financial Transaction Type}</li>
     * <li>{@link Instant creation date}</li>
     * <li>{@link User User id} - the value is taken from the method parameter</li>
     * </ul>
     * The method does not set the <b>id</b> property - the value is set by the database
     *
     * @param count number of elements to create
     * @param type {@link FinancialTransactionType Financial Transaction Type}
     * @param userId
     * @return {@code FinancialTransactionCategoryDTO} list
     */
    public static List<FinancialTransactionCategoryDTO> createFinancialTransactionCategoryDTOListForTest(
            int count,
            FinancialTransactionType type,
            Long userId) {
        final String name = EXAMPLE_CATEGORY_NAME;

        ArrayList<FinancialTransactionCategoryDTO> list = new ArrayList<>(count);
        for (long i = 1; i <= count; i++) {
            list.add(new FinancialTransactionCategoryDTO(
                    i,
                    name + i,
                    type,
                    userId)
            );
        }

        return list;
    }

}
