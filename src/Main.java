import model.benefit.*;
import model.bo.MembershipPlanBO;
import model.enums.MembershipBenefit;
import model.request.CreatePlanRequest;
import model.request.CreatePlanTierBenefitRequest;
import model.request.CreatePlanTierRequest;
import repository.inmemory.*;
import service.plan.MembershipPlanService;
import service.plan.MembershipPlanServiceImplV1;
import service.subscription.SubscriptionService;
import service.subscription.SubscriptionServiceImplV1;
import service.subscription.SubscriptionStatusUpdateService;
import service.subscription.SubscriptionStatusUpdateServiceImplV1;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Scanner sc = new Scanner(System.in);
        try {
            // Repository init
            MembershipPlanRepository planRepository = new MembershipPlanRepository();
            PlanBenefitRepository planBenefitRepository = new PlanBenefitRepository();
            PlanTierRepository planTierRepository = new PlanTierRepository();
            SubscriptionRepository subscriptionRepository = new SubscriptionRepository();
            SubscriptionStatusUpdateEventRepository subscriptionStatusUpdateEventRepository =
                    new SubscriptionStatusUpdateEventRepository();

            // Service init
            MembershipPlanService planService = new MembershipPlanServiceImplV1(planRepository, planTierRepository,
                    planBenefitRepository);
            SubscriptionService subscriptionService = new SubscriptionServiceImplV1(subscriptionRepository,
                    subscriptionStatusUpdateEventRepository, planRepository, planTierRepository);
            SubscriptionStatusUpdateService statusUpdateService = new SubscriptionStatusUpdateServiceImplV1(
                    subscriptionStatusUpdateEventRepository, subscriptionRepository);

            // Entity init
            List<CreatePlanTierRequest> plan1Tiers = List.of(new CreatePlanTierRequest(1, "SILVER", "silver",
                            List.of(new CreatePlanTierBenefitRequest(MembershipBenefit.FREE_DELIVERY,
                                    new PlanBenefitMetadata(new FreeDeliveryBenefitMetadata(0, 50000,
                                            1500, 10000)))), 0),
                    new CreatePlanTierRequest(2, "GOLD", "gold",
                            List.of(new CreatePlanTierBenefitRequest(MembershipBenefit.FREE_DELIVERY,
                                            new PlanBenefitMetadata(new FreeDeliveryBenefitMetadata(0, 50000,
                                                    1500, 10000))),
                                    new CreatePlanTierBenefitRequest(MembershipBenefit.FAST_DELIVERY,
                                            new PlanBenefitMetadata(new FreeDeliveryBenefitMetadata(0, 50000,
                                                    1500, 10000))),
                                    new CreatePlanTierBenefitRequest(MembershipBenefit.PERCENT_DISCOUNT,
                                            new PlanBenefitMetadata(new PercentDiscountBenefitMetadata(10,
                                                    List.of("1", "2", "3"))))), 0),
                    new CreatePlanTierRequest(3, "PLATINUM", "platinum",
                            List.of(new CreatePlanTierBenefitRequest(MembershipBenefit.FREE_DELIVERY,
                                            new PlanBenefitMetadata(new FreeDeliveryBenefitMetadata(0, 50000,
                                                    1500, 10000))),
                                    new CreatePlanTierBenefitRequest(MembershipBenefit.FAST_DELIVERY,
                                            new PlanBenefitMetadata(new FreeDeliveryBenefitMetadata(0, 50000,
                                                    1500, 10000))),
                                    new CreatePlanTierBenefitRequest(MembershipBenefit.PERCENT_DISCOUNT,
                                            new PlanBenefitMetadata(new PercentDiscountBenefitMetadata(10,
                                                    List.of("1", "2", "3")))),
                                    new CreatePlanTierBenefitRequest(MembershipBenefit.EXCLUSIVE_DEALS,
                                            new PlanBenefitMetadata(new ExclusiveDealsBenefitMetadata()))), 0));
            List<CreatePlanTierRequest> plan2Tiers = List.of(new CreatePlanTierRequest(1, "STANDARD", "standard",
                            List.of(new CreatePlanTierBenefitRequest(MembershipBenefit.EXCLUSIVE_COUPONS,
                                    new PlanBenefitMetadata(new ExclusiveCouponsBenefitMetadata(List.of("1", "2"))))), 0));
            MembershipPlanBO plan1 = planService.createPlan(new CreatePlanRequest(0, "FREE_TIER", Duration.ofDays(30L),
                    true, "free tiered plan", plan1Tiers));
            MembershipPlanBO plan2 = planService.createPlan(new CreatePlanRequest(3000, "STANDARD", Duration.ofDays(30L),
                    true, "standard plan", plan2Tiers));

            System.out.println();
            System.out.println(planService.getAllPlans("1"));
        } catch (Exception e) {
            System.out.printf("Failed: %s\n", e.getMessage());
        }
    }
}
