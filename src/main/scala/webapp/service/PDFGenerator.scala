package webapp.service

import java.io.{File, FileOutputStream}

import org.xhtmlrenderer.pdf.ITextRenderer
import webapp.model.packages.Package

import scala.xml.Elem

/**
  * Created by Alfred on 07.02.2021.
  */
trait PDFGenerator {

  def generate(pckg: Package, filePath: String): Unit = {
    val file = new File(filePath)
    val fos = new FileOutputStream(file)
    val renderer = new ITextRenderer()
    renderer.setDocumentFromString(generateXML(pckg).toString)
    renderer.layout()
    renderer.createPDF(fos)
    fos.close()
  }

  private def generateXML(pckg: Package): Elem = {
    val image = "data:image/png;base64, " + pckg.image
    <div>
      <img src={image} style="height:100px;width:100px;"/>
      <div>
        <label>Id:</label>
        <span>{pckg.id.get}</span>
      </div>
      <div>
        <label>Sender Name:</label>
        <span>{pckg.senderName}</span>
      </div>
      <div>
        <label>Sender Surname:</label>
        <span>{pckg.senderSurname}</span>
      </div>
      <div>
        <label>Sender Address:</label>
        <span>{pckg.senderAddress}</span>
      </div>
      <div>
        <label>Sender Phone Number:</label>
        <span>{pckg.senderPhoneNumber}</span>
      </div>
      <div>
        <label>Receiver Name:</label>
        <span>{pckg.receiverName}</span>
      </div>
      <div>
        <label>Receiver Surname:</label>
        <span>{pckg.receiverSurname}</span>
      </div>
      <div>
        <label>Receiver Address:</label>
        <span>{pckg.receiverAddress}</span>
      </div>
      <div>
        <label>Receiver Phone Number:</label>
        <span>{pckg.receiverPhoneNumber}</span>
      </div>
    </div>
  }
}

object PDFGenerator extends PDFGenerator