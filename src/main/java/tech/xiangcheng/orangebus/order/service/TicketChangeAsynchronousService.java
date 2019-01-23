package tech.xiangcheng.orangebus.order.service;

import tech.xiangcheng.orangebus.leaf.util.json.JsonObj;

public interface TicketChangeAsynchronousService {
	/**
	 * 
	 * @param newOrderParams
	 * @param oldOrderParams
	 */
	void submitTicketChange(JsonObj newOrderParams, JsonObj oldOrderParams);
	/**
	 * 微信支付回调时调用
	 * @param ticketCode
	 * @param openid TODO
	 */
	void submitTicketPaid(String ticketCode, String openid);
	/**
	 * 判断是否为改签票
	 * @param ticketCode
	 * @return
	 */
	boolean isChangeOrder(String ticketCode);
}
