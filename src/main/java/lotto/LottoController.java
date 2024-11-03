package lotto;

import lotto.model.lotto.Lotto;
import lotto.model.lotto.LottoMachine;
import lotto.model.lotto.LottoPublisher;
import lotto.model.lotto_result.DrawNumbers;
import lotto.model.lotto_result.DrawNumbersBuilder;
import lotto.number_generator.LottoNumberGenerator;
import lotto.number_generator.NumberGenerator;
import lotto.view.InputView;

import java.util.List;

public class LottoController {

    private final InputView inputView;
    private final OutputView outputView;
    private LottoMachine lottoMachine;
    private LottoPublisher lottoPublisher;

    public LottoController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void buyLotto() {

        NumberGenerator lottoNumberGenerator = new LottoNumberGenerator();
        lottoPublisher = new LottoPublisher(lottoNumberGenerator);

        long payment = getPayment();
        long lottoCount = calcLottoCount(payment);
        System.out.println("\n" + lottoCount + "개를 구매했습니다.");

        List<Lotto> lottos = lottoPublisher.publishLotto(lottoCount);
        outputView.printLottos(lottos);

        DrawNumbers drawNumbers = getDrawNumbers();
        lottoMachine = new LottoMachine(drawNumbers);

        double revenueRate = lottoMachine.examineLotto(lottos, lottoCount);
        System.out.println(outputView.resultToString(revenueRate));
    }

    private static long calcLottoCount(long payment) {
        return payment / Lotto.PRICE;
    }

    private long getPayment() {
        long payment = 0;
        boolean isInvalidInput = true;
        while (isInvalidInput) {
            try {
                payment = inputView.getPayment();
                isInvalidInput = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return payment;
    }

    private DrawNumbers getDrawNumbers() {
        DrawNumbersBuilder builder = new DrawNumbersBuilder();

        builder = buildWinningNumber(builder);
        builder = buildBonusNumber(builder);

        return builder.build();
    }

    private DrawNumbersBuilder buildWinningNumber(DrawNumbersBuilder builder) {
        boolean isInvalidInput = true;
        while (isInvalidInput) {
            try {
                builder = inputView.getWinningNumbers(builder);
                isInvalidInput = false;
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return builder;
    }

    private DrawNumbersBuilder buildBonusNumber(DrawNumbersBuilder builder) {
        boolean isInvalidInput = true;
        while (isInvalidInput) {
            try {
                builder = inputView.getBonusNumber(builder);
                isInvalidInput = false;
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return builder;
    }
}