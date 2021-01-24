package com.ai.st.microservice.administration.dto.notifier;

import java.io.Serializable;

public class MicroserviceNotificationRecoverAccountDto implements Serializable {

	private static final long serialVersionUID = -3073283184260792970L;

	private Long userCode;
	private String email;
	private String type;
	private int status;

	private String code;
	private String expirationDate;

	private String siteURL;
	private String siteEmail;

	public MicroserviceNotificationRecoverAccountDto() {

	}

	public Long getUserCode() {
		return userCode;
	}

	public void setUserCode(Long userCode) {
		this.userCode = userCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSiteURL() {
		return siteURL;
	}

	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

	public String getSiteEmail() {
		return siteEmail;
	}

	public void setSiteEmail(String siteEmail) {
		this.siteEmail = siteEmail;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getSubject() {
		return "Notificación Sistema de Transición Barrido Predial – Recuperar Cuenta";
	}

	public String getBody() {
		String html = "";
		html += "<div>El Sistema de Transición para el Barrido Predial en Colombia le informa:</div><br>";
		html += "<div>Que se ha generado un código único " + code
				+ " para que pueda recupera su cuenta, el cual deberá ingresar junto con su nueva contraseña y el cual expirará "
				+ this.expirationDate + " </div><br>";

		html += "<div>Nota: Cualquier inquietud o inconveniente en el ingreso a la plataforma por favor comunicarse con el siguiente correo: "
				+ siteEmail + "</div>";
		html += "<br><div>--</div>";
		html += "SISTEMA DE TRANSICIÓN</div>";
		return html;
	}

}
