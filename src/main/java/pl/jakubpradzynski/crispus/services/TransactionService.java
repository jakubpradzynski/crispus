package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.repositories.*;
import pl.jakubpradzynski.crispus.utils.DateUtils;

import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.List;

/**
 * A service-type class related to transactions.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Creates new user's transaction by call a function in Transaction RepositoryClass.
     * @param transactionDto - data about new transaction
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    @Transactional
    public void addNewUserTransaction(TransactionDto transactionDto) throws ParseException {
        User user = userRepository.getUserByEmail(transactionDto.getUsername());
        transactionRepository.createTransaction(
                transactionDto.getDescription(),
                user,
                accountRepository.getUserAccountByName(user, transactionDto.getAccountName()),
                transactionDto.getValue(),
                DateUtils.stringToDate(transactionDto.getDate(), "yyyy-MM-dd"),
                !transactionDto.getPlaceName().equals("") ? placeRepository.getPlaceByName(transactionDto.getPlaceName()) : null,
                !transactionDto.getTransactionCategoryName().equals("") ? categoryRepository.getCategoryByName(transactionDto.getTransactionCategoryName()) : null
        );
    }

    /**
     * Method returns info about transaction specific by id after calling Transaction RepositoryClass.
     * @param id - transaction id
     * @return TransactionDto
     */
    public TransactionDto getTransactionDtoById(Integer id) {
        return TransactionDto.fromTransaction(transactionRepository.getById(id));
    }

    /**
     * Method change data of transaction specific by id to given data after calling Transaction RepositoryClass.
     * @param id - transaction id
     * @param transactionDto - edited transaction
     * @throws ParseException - Exception is thrown when it is impossible to parse the date from the string.
     */
    @Transactional
    public void editTransactionById(Integer id, TransactionDto transactionDto) throws ParseException {
        transactionRepository.updateTransaction(id, transactionDto);
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns list of user's transactions in range after receive this data from Transaction RepositoryClass.
     * @param username - user's email
     * @param start - start of range
     * @param max - max number in range
     * @return List of TransactionDto
     */
    public List<TransactionDto> getUserTransactionByRange(String username, Integer start, Integer max) {
        User user = userRepository.getUserByEmail(username);
        return transactionRepository.getUserTransactionByRange(user, start, max);
    }

    /**
     * Method removes transaction specific by id by call a function in Transaction RepositoryClass.
     * @param id - transaction id
     */
    @Transactional
    public void removeTransactionById(Integer id) {
        transactionRepository.remove(id);
    }
}
