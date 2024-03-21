package com.example.demo.src.payment;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.article.model.PatchArticleReq;
import com.example.demo.src.payment.model.PostPaymentRes;
import com.example.demo.src.payment.model.SubscriptionRes;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.common.response.BaseResponseStatus.UNEXPECTED_ERROR;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/app/payments")
public class PaymentController {

    private final JwtService jwtService;
    private final PaymentsService paymentService;



    /**
     * 결제 검증 API
     * 결제 진행 후 이 API를 호출하여 결제를 검증합니다. 검증 성공 시 결제가 완료됩니다.
     * [POST] /app/payments/verify/{impUid}
     * @param impUid 결제 고유 번호
     * @return BaseResponse<PostPaymentRes>
     */
    @Operation(
            summary = "결제 검증 API",
            description = "결제 아이디를 통해 결제를 검증하고 처리합니다. # Header에 `X-ACCESS-TOKEN`이 필요합니다. `Path Variable`로 결제 아이디를 입력 하세요. 결제 진행 후 결제 완료 API를 호출 하세요. 검증 후 통과 하지 못하면 결제 취소, 통과 하면 결제 완료 됩니다.",
            security = {@SecurityRequirement(name = "X-ACCESS-TOKEN")},
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = PostPaymentRes.class))),
                    @ApiResponse(description = "인증 실패 또는 기타 오류", responseCode = "400")
            }
    )
    @PostMapping("/verify/{impUid}")
    public BaseResponse<PostPaymentRes> verifyPayment(@PathVariable String impUid) {
        try {
            // 유저 ID는 JWT 토큰에서 추출합니다. 실제 구현에서는 토큰 검증 로직을 통해 ID를 추출해야 합니다.
            Long userId = jwtService.getUserId();

            // PaymentService의 결제 검증 메소드를 호출합니다.
            PostPaymentRes postPaymentRes = paymentService.verifyAndProcessPayment(impUid, userId);

            return new BaseResponse<>(postPaymentRes);

        } catch (Exception e) {
            return new BaseResponse<>(UNEXPECTED_ERROR);
        }


    }

    /**
     * 구독 조회 API
     * [GET] /app/payments/subscription
     * 이 API는 사용자의 구독 상태 및 정보를 조회합니다. 사용자의 최근 결제 내역을 검사하여, 현재 활성화된 구독 정보를 반환합니다.
     * 사용자는 이 API를 통해 자신의 구독 여부 및 구독 기간 등의 상세 정보를 확인할 수 있습니다.
     * Header에 `X-ACCESS-TOKEN`이 필요하며, 토큰을 통해 사용자 식별이 이루어집니다.
     */
    @Operation(
            summary = "구독 조회 API",
            description = "사용자의 구독 정보를 조회합니다. 사용자는 자신의 구독 상태 및 기간 등의 상세 정보를 확인할 수 있습니다. Header에 `X-ACCESS-TOKEN`을 포함해야 합니다.",
            security = @SecurityRequirement(name = "X-ACCESS-TOKEN"),
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = SubscriptionRes.class))),
                    @ApiResponse(description = "인증 실패 또는 구독 정보 없음", responseCode = "400")
            }
    )
    @GetMapping("/subscription")
    public BaseResponse<SubscriptionRes> getSubscriptionInfo() {
        Long jwtUserId = jwtService.getUserId();

        SubscriptionRes subscriptionRes = paymentService.getSubscriptionInfo(jwtUserId);

        return new BaseResponse<>(subscriptionRes);

    }

}