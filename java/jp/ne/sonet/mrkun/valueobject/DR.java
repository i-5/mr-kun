package jp.ne.sonet.mrkun.valueobject;

import java.util.*;
import org.jdom.*;

/**
 * This class models information related to a specific Doctor. It is used as
 * a value object for the purpose of transferring details between the
 * DR_ProfileCtlr servlet, the DRManager session bean and the dr10.jsp
 * view template.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: DR.java,v 1.1.1.1.2.10 2001/09/12 08:35:49 rick Exp $
 */
public class DR extends User
{
  private String        drId;
  private String        systemDrCd;
  private String        hospital;
  private String        password;
  private String        medCertification;
  private Boolean       publicOfficial;
  private String        workArea;
  private String        usageArea;
  private String        networkEnv;
  private String        specialty;
  private String        email;
  private String        reEmail; // What the hell is this doing here ???!!!???? RK
  private Map           mrList;
  private Boolean       mrKunMail;
  private Boolean       emailInfo;
  private Integer       currentUsagePoint;
  private Integer       oldUsagePoint;

  public DR()
  {
    mrList = new Hashtable();
  }

  public String getSystemDrCd()
  {
    return this.systemDrCd;
  }

  public String getHospital()
  {
    return this.hospital;
  }

  public String getPassword()
  {
    return this.password;
  }

  public String getMedCertification()
  {
    return this.medCertification;
  }

  public Boolean isPublicOfficial()
  {
    return this.publicOfficial;
  }

  public String getWorkArea()
  {
    return this.workArea;
  }

  public String getUsageArea()
  {
    return this.usageArea;
  }

  public String getNetworkEnv()
  {
    return this.networkEnv;
  }

  public String getSpecialty()
  {
    return this.specialty;
  }

  public String getEmail()
  {
    return this.email;
  }

  public String getReEmail()
  {
    return this.reEmail;
  }

  public String getDrId()
  {
    return this.drId;
  }

  public Boolean getMRKunMail()
  {
    return this.mrKunMail;
  }

  public Boolean getEmailInfo()
  {
    return this.emailInfo;
  }

  public Integer getUsagePoint()
  {
    return new Integer(this.currentUsagePoint.intValue() + this.oldUsagePoint.intValue());
  }

  public Integer getCurrentUsagePoint()
  {
    return this.currentUsagePoint;
  }

  public Integer getOldUsagePoint()
  {
    return this.oldUsagePoint;
  }

  public void setDrId(String drId)
  {
    this.drId = drId;
  }

  public void setSystemDrCd(String systemDrCd)
  {
    this.systemDrCd = systemDrCd;
  }

  public void setHospital(String hospital)
  {
    this.hospital = hospital;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public void setMedCertification(String medCertification)
  {
    this.medCertification = medCertification;
  }

  public void setPublicOfficial(Boolean publicOfficial)
  {
    this.publicOfficial = publicOfficial;
  }

  public void setWorkArea(String workArea)
  {
    this.workArea = workArea;
  }

  public void setUsageArea(String usageArea)
  {
    this.usageArea = usageArea;
  }

  public void setNetworkEnv(String networkEnv)
  {
    this.networkEnv = networkEnv;
  }

  public void setSpecialty(String specialty)
  {
    this.specialty = specialty;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setReEmail(String reEmail)
  {
    this.reEmail = reEmail;
  }

  public Map getMRProfileMap()
  {
    return this.mrList;
  }

  public void setMrProfileMap(Map mrList)
  {
    this.mrList = mrList;
  }

  public List getMRIdList()
  {
    return Arrays.asList(this.mrList.keySet().toArray());
  }
  
  public MRInformation getMrInformation(String mrId)
  {
    return (MRInformation)this.mrList.get(mrId);
  }

  public void addMRInformation(MRInformation mri)
  {
    this.mrList.put(mri.getMrId(), mri);
  }

  public void deleteMRInformation(String mrId)
  {
    this.mrList.remove(mrId);
  }

  public Map getMRList()
  {
    return this.mrList;
  }

  public void setMRList(Map mrList)
  {
    this.mrList = mrList;
  }

  public void setMRKunMail(Boolean mrKunMail)
  {
    this.mrKunMail = mrKunMail;
  }

  public void setEmailInfo(Boolean emailInfo)
  {
    this.emailInfo = emailInfo;
  }

  public void setOldUsagePoint(Integer oldUsagePoint)
  {
    this.oldUsagePoint = oldUsagePoint;
  }

  public void setCurrentUsagePoint(Integer currentUsagePoint)
  {
    this.currentUsagePoint = currentUsagePoint;
  }

  public String getHelp()
  {
    return "DR class help message - hi there";
  }

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);

    if (getDrId() != null)
      masterElement.addContent(new Element("drId").setText(getDrId()));

    if (getSystemDrCd() != null)
      masterElement.addContent(new Element("systemDrCd").setText(getSystemDrCd()));

    if (getHospital() != null)
      masterElement.addContent(new Element("hospital").setText(getHospital()));

    if (getPassword() != null)
      masterElement.addContent(new Element("password").setText(getPassword()));

    if (getMedCertification() != null)
      masterElement.addContent(new Element("medCertification").setText(getMedCertification()));

    if (isPublicOfficial() != null)
      masterElement.addContent(new Element("isPublicOfficial").setText("" + isPublicOfficial().booleanValue()));

    if (getWorkArea() != null)
      masterElement.addContent(new Element("workArea").setText(getWorkArea()));

    if (getUsageArea() != null)
      masterElement.addContent(new Element("usageArea").setText(getUsageArea()));

    if (getNetworkEnv() != null)
      masterElement.addContent(new Element("networkEnv").setText(getNetworkEnv()));

    if (getSpecialty() != null)
      masterElement.addContent(new Element("specialty").setText(getSpecialty()));

    if (getEmail() != null)
      masterElement.addContent(new Element("email").setText(getEmail()));

    if (getReEmail() != null)
      masterElement.addContent(new Element("remail").setText(getReEmail()));

    if (this.getMRProfileMap() != null)
      masterElement.addContent(mapToXML("mrProfileList", getMRProfileMap(), "mrProfile"));

    if (getMRKunMail() != null)
      masterElement.addContent(new Element("mrkunMail").setText("" + getMRKunMail().booleanValue()));

    if (getEmailInfo() != null)
      masterElement.addContent(new Element("emailInfo").setText("" + getEmailInfo().booleanValue()));

    if (getCurrentUsagePoint() != null)
      masterElement.addContent(new Element("currentUsagePoint").setText("" + getOldUsagePoint().intValue()));

    if (getOldUsagePoint() != null)
      masterElement.addContent(new Element("oldUsagePoint").setText("" + getOldUsagePoint().intValue()));

    return masterElement;
  }
}
