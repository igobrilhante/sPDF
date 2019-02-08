package io.github.cloudify.scala.spdf

import org.scalatest.Matchers
import org.scalatest.WordSpec

class PdfConfigSpec extends WordSpec with Matchers {

  "PdfConfig" should {

    "have a default config" in {
      PdfConfig.toParameters(PdfConfig.default) should equal(Seq("--encoding", "UTF-8"))
    }

    "generate parameters from config" in {
      val config = new PdfConfig {
        enableForms := true
        marginBottom := "1in"
        minimumFontSize := 3
        orientation := Landscape
        zoom := 1.23f
      }
      PdfConfig.toParameters(config) should equal(Seq("--enable-forms", "--encoding", "UTF-8", "--margin-bottom", "1in", "--minimum-font-size", "3", "--orientation", "Landscape", "--zoom", "%.2f".format(1.23f)))
    }

    "no pdf compression" in {
      val config = new PdfConfig {
        noPdfCompression := true
      }

      PdfConfig.toParameters(config) should contain("--no-pdf-compression")
    }

    "generate the --allow parameter" in {
      val config = new PdfConfig {
        allow := List("/some/path", "/some/other/path")
      }
      PdfConfig.toParameters(config) should equal(Seq("--allow", "/some/path", "--allow", "/some/other/path", "--encoding", "UTF-8"))
    }

    "print media type" in {
      val config = new PdfConfig {
        printMediaType := Some(true)
      }
      PdfConfig.toParameters(config) should contain("--print-media-type")
    }

    "no print media type" in {
      val config = new PdfConfig {
        printMediaType := Some(false)
      }
      PdfConfig.toParameters(config) should contain("--no-print-media-type")
    }

    "use x-server" in {
      val config = new PdfConfig {
        useXServer := true
      }
      PdfConfig.toParameters(config) should contain("--use-xserver")
    }

    "check for executable in PATH" in {
      PdfConfig.findExecutable match {
        case Some(path) => path.contains("wkhtmltopdf") should equal(true)
        case None => true should equal(true)
      }
    }

    "generate the --custom-header parameter" in {
      val config = new PdfConfig {
        customHeader := "Authorization \"Basic ectectect\""
        customHeaderPropagation := true
        noCustomHeaderPropagation := true
      }
      PdfConfig.toParameters(config) should equal(Seq("--custom-header", "Authorization", "\"Basic ectectect\"", "--custom-header-propagation", "--no-custom-header-propagation", "--encoding", "UTF-8"))
    }    
  }

}
