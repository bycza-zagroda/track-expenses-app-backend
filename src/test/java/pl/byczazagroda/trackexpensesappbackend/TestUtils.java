package pl.byczazagroda.trackexpensesappbackend;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class TestUtils {

    public static final Long ID_1L = 1L;

    public static final Long USER_ID_1L = 1L;

    public static final Long WALLET_ID_1L = 1L;

    public static final Long FINANCIAL_TRANSACTION_ID_1L = 1L;

    public static final Long FINANCIAL_TRANSACTION_CATEGORY_ID_1L = 1L;

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
    public static List<FinancialTransactionCategory> createFinancialTransactionCategoryListForTest(int count) {
        final String name = "Example category name_";
        final FinancialTransactionType financialTransactionType = FinancialTransactionType.INCOME;

        ArrayList<FinancialTransactionCategory> list = new ArrayList<>(count);
        for (long i = 1; i <= count; i++) {
            list.add(FinancialTransactionCategory.builder()
                    .id(i)
                    .name(name + count)
                    .type(financialTransactionType)
                    .creationDate(Instant.now())
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
     * @return {@code FinancialTransactionCategoryDTO} list
     */
    public static List<FinancialTransactionCategoryDTO> createFinancialTransactionCategoryDTOListForTest(int count, Long userId) {
        final String categoryName = "Example category DTO name_";
        final FinancialTransactionType categoryType = FinancialTransactionType.INCOME;

        ArrayList<FinancialTransactionCategoryDTO> list = new ArrayList<>(count);
        for (long id = 1; id <= count; id++) {
            list.add(new FinancialTransactionCategoryDTO(
                    id,
                    categoryName + id,
                    categoryType,
                    userId)
            );
        }

        return list;
    }

}
