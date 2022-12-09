package mainservice.controllers.closed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.request.dto.RequestDto;
import mainservice.request.service.RequestServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class RequestController {
    private final RequestServiceImpl requestService;

    @PostMapping("/{userId}/requests")
    public RequestDto saveRequest(@PathVariable Long userId,
                                  @RequestParam(name = "eventId", required = false) Long eventId) {
        log.info("Creating request userId={}, eventId={}", userId, eventId);
        return requestService.saveRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable(name = "userId") Long userId,
                                     @PathVariable(name = "eventId") Long eventId,
                                     @PathVariable(name = "reqId") Long reqId) {
        log.info("Req={} confirm on event={} for user={}", reqId, eventId, userId);
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable(name = "userId") Long userId,
                                    @PathVariable(name = "eventId") Long eventId,
                                    @PathVariable(name = "reqId") Long reqId) {
        log.info("Req={} reject on event={} for user={}", reqId, eventId, userId);
        return requestService.rejectRequestByOwner(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequestByRequester(@PathVariable(name = "userId") Long userId,
                                               @PathVariable(name = "requestId") Long reqId) {
        log.info("Cancel request={} by requester={}", reqId, userId);
        return requestService.cancelRequestByRequester(userId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getForUserHisRequests(@PathVariable(name = "userId") Long userId) {
        log.info("Get requests for requester={}", userId);
        return requestService.getForUserHisRequests(userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getInfoAboutRequestsForEventOwner(@PathVariable(name = "userId") Long userId,
                                                              @PathVariable(name = "eventId") Long eventId) {
        log.info("Get for req for event={} owner={}", eventId, userId);
        return requestService.getInfoAboutRequestsForEventOwner(userId, eventId);
    }
}
