//package uz.devops.partnerms.service.resolver;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.transaction.annotation.Transactional;
//import uz.devops.enumeration.CardType;
//import uz.devops.partnerms.IntegrationTest;
//import uz.devops.partnerms.config.ApplicationProperties;
//import uz.devops.partnerms.domain.CorpCard;
//import uz.devops.partnerms.domain.Partner;
//import uz.devops.partnerms.domain.enumeration.EntityType;
//import uz.devops.partnerms.domain.enumeration.Status;
//import uz.devops.partnerms.repository.CorpCardRepository;
//import uz.devops.partnerms.repository.EmployeeCardRepository;
//import uz.devops.partnerms.repository.PartnerRepository;
//import uz.devops.partnerms.service.mq.ProfileRpcClient;
//import uz.devops.service.dto.CreateVirtualCardRequestDTO;
//
//import javax.persistence.EntityManager;
//import java.time.*;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@IntegrationTest
//class CardServiceResolverTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private CardServiceResolver cardServiceResolver;
//
//    @Autowired
//    private PartnerRepository partnerRepository;
//
//    @Autowired
//    private CorpCardRepository corpCardRepository;
//
//    @Autowired
//    private ApplicationProperties applicationProperties;
//
//    @Autowired
//    private EmployeeCardRepository employeeCardRepository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @MockBean
//    private ProfileRpcClient profileRpcClient;
//
//    private static final String INN = "605040404";
//    private static final String DEVOPS = "DevOps";
//    private static final String PHONE_NUMBER = "998979559770";
//
//    @Test
//    @Transactional
//    void resolveCreateVirtualCardToSuccess() throws JsonProcessingException {
//
//        createPartner();
//        mockPartnerRelToSuccess();
//        var baseResult = cardServiceResolver.resolveCreateVirtualCard(new CreateVirtualCardRequestDTO(INN, PHONE_NUMBER, CardType.TOP_CARD));
//        System.out.println("RESULT: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseResult));
//
//        assertThat(baseResult).isNotNull();
//        assertThat(baseResult.getSuccess()).isTrue();
//
//        System.out.println("\n\n");
//        var corpCards = corpCardRepository.findAllByPartnerInn(INN);
//        System.out.println("VIRTUAL CARD: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(corpCards.get(0)));
//        assertThat(corpCards).isNotEmpty();
//        assertThat(corpCards.size()).isGreaterThanOrEqualTo(1);
//        assertThat(corpCards.get(0).getToken()).isNotBlank();
//        assertThat(corpCards.get(0).getOwnerName()).isEqualToIgnoringCase(DEVOPS);
//        assertThat(corpCards.get(0).getExpiryDate()).isBeforeOrEqualTo(LocalDateTime.now().plusYears(3L).toInstant(ZoneOffset.UTC));
//        assertThat(corpCards.get(0).getStatus()).isEqualTo(Status.NEW.toString());
//        assertThat(corpCards.get(0).getType()).isEqualByComparingTo(CardType.TOP_CARD);
//        assertThat(corpCards.get(0).getPartner()).isNotNull();
//        assertThat(corpCards.get(0).getPartner().getInn()).isEqualTo(INN);
//        assertThat(corpCards.get(0).getVirtualPan()).matches(applicationProperties.getVirtualCard().getPan());
//
//        System.out.println("\n\n");
//        var employeeCards = employeeCardRepository.findAll();
//        System.out.println("EMPLOYEE CARDS: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeeCards));
//        assertThat(employeeCards).isNotEmpty();
//        assertThat(employeeCards.size()).isGreaterThanOrEqualTo(1);
//        assertThat(employeeCards.get(0).getCardToken()).isNotBlank();
//        assertThat(employeeCards.get(0).getUsername()).isEqualToIgnoringCase(PHONE_NUMBER);
//    }
//
//    @Test
//    @Transactional
//    void resolveCreateVirtualCardToFailed() throws JsonProcessingException {
//        var baseResult = cardServiceResolver.resolveCreateVirtualCard(new CreateVirtualCardRequestDTO(INN, PHONE_NUMBER, CardType.UPECO));
//        System.out.println("RESULT: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(baseResult));
//        assertThat(baseResult).isNotNull();
//        assertThat(baseResult.getSuccess()).isFalse();
//    }
//
//    @Test
//    @Transactional
//    void checkSequenceTest() {
//        CorpCard virtualCard1 = new CorpCard();
//        virtualCard1.setStatus(Status.NEW.toString());
//        virtualCard1.setToken(UUID.randomUUID().toString());
//        virtualCard1.setType(CardType.TOP_CARD);
//        virtualCard1.setVirtualPan(getGeneratedPan());
//
//        CorpCard virtualCard2 = new CorpCard();
//        virtualCard2.setStatus(Status.NEW.toString());
//        virtualCard2.setToken(UUID.randomUUID().toString());
//        virtualCard2.setType(CardType.UPECO);
//        virtualCard2.setVirtualPan(getGeneratedPan());
//
//        var corpCards = corpCardRepository.saveAll(List.of(virtualCard1, virtualCard2));
//        assertThat(corpCards).isNotEmpty();
//        assertThat(corpCards.size()).isGreaterThanOrEqualTo(2);
//        for (CorpCard corpCard : corpCards) {
//            assertThat(corpCard.getVirtualPan()).matches(applicationProperties.getVirtualCard().getPan());
//        }
//    }
//
//    @Test
//    @Transactional
//    void selectNextValTest() {
//        String generatedPan = getGeneratedPan();
//        assertThat(generatedPan).isNotBlank();
//        assertThat(generatedPan).matches(applicationProperties.getVirtualCard().getPan());
//        assertThat(generatedPan.length()).isEqualTo(16);
//    }
//
//    String getGeneratedPan() {
//        return entityManager.createNativeQuery("SELECT nextval('virtual_pan_sequence') as num").getSingleResult().toString();
//    }
//
//    void createPartner() {
//        Partner partner = new Partner();
//        partner.setType(EntityType.LEGAL_ENTITY);
//        partner.setInn(INN);
//        partner.setName(DEVOPS);
//        partner.setRegistrationDate(LocalDate.now().minus(1, ChronoUnit.DAYS));
//        partner.setSignedDate(Instant.now());
//        partnerRepository.save(partner);
//    }
//
//    void mockPartnerRelToSuccess() {
//        Mockito.when(profileRpcClient.checkRelation(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(true);
//    }
//}
