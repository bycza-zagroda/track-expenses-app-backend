package pl.byczazagroda.trackexpensesappbackend;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// todo Refactor: In the end of the task rename class name to TestUtils
public final class IntegrationTestUtils {

    public static final Long ID_1L = 1L;

    public static final Long USER_ID_1L = 1L;

    public static final Long WALLET_ID_1L = 1L;

    public static final Long FINANCIAL_TRANSACTION_ID_1L = 1L;

    public static final Long FINANCIAL_TRANSACTION_CATEGORY_ID_1L = 1L;

    private IntegrationTestUtils() {
    }

    /**
     * Create one test {@link User} with specific properties:
     * <ul>
     * <li>{@link String name}</li>
     * <li>{@link String email}</li>
     * <li>{@link String password}</li>
     * <li>{@link UserStatus status} with value {@link UserStatus#VERIFIED VERIFIED}</li>
     * </ul>
     * The method does not set the <b>id</b> property - the value is set by the database
     *
     * @param repository {@code UserRepository}
     * @return {@code User}
     */
    public static User createTestUser(UserRepository repository) {
        User user = User.builder()
                .userName("UserOne")
                .email("user@server.domain.com")
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();

        return (repository != null) ? repository.save(user) : user;
    }

    /**
     * Create one test {@link User} with specific properties:
     * <ul>
     * <li>{@link String name}</li>
     * <li>{@link String email}</li> - the value is taken from the method parameter</li>
     * <li>{@link String password}</li>
     * <li>{@link UserStatus status} with value {@link UserStatus#VERIFIED VERIFIED}</li>
     * </ul>
     * The method does not set the <b>id</b> property - the value is set by the database
     *
     * @param repository {@code UserRepository}.
     * @param email      {@code String}
     * @return {@code User}
     */
    public static User createTestUserWithEmail(UserRepository repository, String email) {
        final User user = User.builder()
                .userName("UserOne")
                .email(email)
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();

        return (repository != null) ? repository.save(user) : user;
    }

    /**
     * Create one test {@link Wallet} with specific properties:
     * <ul>
     * <li>{@link User user}</li>
     * <li>{@link Instant creation date}</li>
     * <li>{@link String name}</li>
     * </ul>
     * The method does not set the <b>id</b> property - the value is set by the database
     *
     * @param repository {@code WalletRepository}.
     * @param user       {@code User}
     * @return {@code Wallet}
     */
    public static Wallet createTestWallet(WalletRepository repository, User user) {
        final Wallet wallet = Wallet.builder()
                .user(user)
                .creationDate(Instant.now())
                .name("Example wallet name")
                .build();

        return (repository != null) ? repository.save(wallet) : wallet;
    }

    /**
     * Create test users list {@link User} with specific properties:
     * <ul>
     * <li>{@link String name}</li>
     * <li>{@link String email}</li>
     * <li>{@link String password}</li>
     * <li>{@link UserStatus status}</li>
     * </ul>
     * The method does not set the <b>id</b> property - the value is set by the database
     *
     * @param count      number of elements to create.
     * @param repository {@code UserRepository}
     * @return {@code User} list
     */
    public static List<User> createTestUserList(int count, UserRepository repository) {
        List<User> list = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            list.add(createTestUser(repository));
        }

        return (repository != null) ? repository.saveAll(list) : list;
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
     * @param count      number of elements to create.
     * @param repository {@code FinancialTransactionCategoryRepository}
     * @return {@code FinancialTransactionCategory} list
     */
    public static List<FinancialTransactionCategory> createFinancialTransactionCategoryList(
            int count,
            FinancialTransactionCategoryRepository repository) {
        final String name = "Example category name_";
        final FinancialTransactionType financialTransactionType = FinancialTransactionType.INCOME;

        ArrayList<FinancialTransactionCategory> list = new ArrayList<>(count);
        for (long i = 1; i <= count; i++) {
            list.add(FinancialTransactionCategory.builder()
                    .name(name + count)
                    .type(financialTransactionType)
                    .creationDate(Instant.now())
                    .build()
            );
        }

        return (repository != null) ? repository.saveAll(list) : list;
    }

    /**
     * Create {@link FinancialTransactionCategoryDTO Financial Transactions Category DTO} list with specific properties:
     * <ul>
     * <li>{@link String Financial Transactions Category name}</li>
     * <li>{@link FinancialTransactionType Financial Transaction Type}</li>
     * <li>{@link Instant creation date}</li>
     * <li>{@link User User id} with value 1L</li>
     * </ul>
     * The method does not set the <b>id</b> property - the value is set by the database
     *
     * @param count number of elements to create
     * @return {@code FinancialTransactionCategoryDTO} list
     */
    public static List<FinancialTransactionCategoryDTO> createFinancialTransactionCategoryDTOList(int count) {
        final String categoryName = "Example category DTO name_";
        final FinancialTransactionType categoryType = FinancialTransactionType.INCOME;

        ArrayList<FinancialTransactionCategoryDTO> list = new ArrayList<>(count);
        for (long id = 1; id <= count; id++) {
            list.add(new FinancialTransactionCategoryDTO(
                    id,
                    categoryName + id,
                    categoryType,
                    USER_ID_1L)
            );
        }

        return list;
    }

}
