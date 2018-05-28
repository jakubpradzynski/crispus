package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.repositories.*;
import pl.jakubpradzynski.crispus.utils.DateUtils;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    CategoryRepository categoryRepository;

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

    public TransactionDto getTransactionDtoById(Integer id) {
        return TransactionDto.fromTransaction(transactionRepository.getTransactionById(id));
    }

    @Transactional
    public void editTransactionById(Integer id, TransactionDto transactionDto) throws ParseException {
        transactionRepository.updateTransaction(id, transactionDto);
    }

    public List<TransactionDto> getUserTransactionByRange(String username, Integer start, Integer max) {
        User user = userRepository.getUserByEmail(username);
        return transactionRepository.getUserTransactionByRange(user, start, max);
    }

    @Transactional
    public void removeTransactionById(Integer id) {
        transactionRepository.removeTransaction(id);
    }
}
