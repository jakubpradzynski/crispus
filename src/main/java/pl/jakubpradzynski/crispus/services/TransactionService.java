package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.repositories.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    TransactionCategoryRepository transactionCategoryRepository;

    @Transactional
    public void addNewUserTransaction(TransactionDto transactionDto) throws ParseException {
        User user = userRepository.getUserByEmail(transactionDto.getUsername());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        transactionRepository.createTransaction(
                transactionDto.getDescription(),
                user,
                accountRepository.getUserAccountByName(user, transactionDto.getAccountName()),
                transactionDto.getValue(),
                format.parse(transactionDto.getDate()),
                !transactionDto.getPlaceDescription().equals("") ? placeRepository.getPlaceByDescription(transactionDto.getPlaceDescription()) : null,
                !transactionDto.getTransactionCategoryName().equals("") ? transactionCategoryRepository.getTransactionCategoryByName(transactionDto.getTransactionCategoryName()) : null
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
