package org.taktik.freehealth.middleware.web.controllers

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner
import org.taktik.freehealth.middleware.MyTestsConfiguration
import org.taktik.freehealth.middleware.dto.genins.InsurabilityInfoDto
import java.io.File
import java.time.Instant

@RunWith(SpringRunner::class)
@Import(MyTestsConfiguration::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GenInsControllerTest : EhealthTest() {
    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private val restTemplate: TestRestTemplate? = null


    private val nisses = mapOf(
        100 to listOf("79090527932","50011440637","36081126424","17011612777","790905 217M93","59041744620","68040413141","72060416706","79090527932","97031458484","18010312870","29031620344","31051928426","17011612777","29041042905","02041306532","88082528989","720604015M60","58042644917","13102125767"),
        300 to listOf("61061423190","52072113288","33072519291","17011701166","0121974140661","24110207459","61100611586","57010179489","78052535583","26020325521","49021629574","95031045894","94042125550","17011312473","61031739707","69072541839","72121339535","621085E+12","89041331132","92062115525"),
        500 to listOf("09052224428","50000148253","33040808211","17011631979","0819018511509","48112112044","98092035007","02040505390","84021730491","56040313113","42012609415","51021908641","63040240711","17011809450","33040808211","85010536219","82091014381","810921544219","82080517003","00000000000"),
        600 to listOf("45021812602","72072320188","16020808228","15011820591","0615007639744","54071402460","27101406159","86052640376","45021812602","92070850968","26120934416","51120124705","48062301752","14011618454","42032920621","88091034505","??","0609003009338","54071402460","27101406159"),
        900 to listOf("73050819368","50010403034","19081826340","17012401843","0446347301700","28030407427","81082855769","50010403034","73050819368","26011100128","24060902854","95021931359","91020551103","17012401843","31011514068","96020250510","51010604775","0386015000200","28030407427","11011238210")
    )
    private fun getNisses(idx: Int) = listOf(nisses[100]!![idx], nisses[300]!![idx], nisses[500]!![idx], nisses[600]!![idx], nisses[900]!![idx])


    @Before
    fun setUp() {
        try {
            System.setProperty("mycarenet.license.password", this.javaClass.getResourceAsStream("/org/taktik/freehealth/middleware/mycarenet.license").reader(Charsets.UTF_8).readText()) } catch (e: NullPointerException) {
            System.setProperty("mycarenet.license.password", File("src/test/resources/org/taktik/freehealth/middleware/mycarenet.license").reader(Charsets.UTF_8).readText())
        }
    }

    @Test
    fun getGeneralInsurability() {
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)
        val genIns = this.restTemplate.exchange("http://localhost:$port/genins/${"74010414733"}?keystoreId=$keystoreId&tokenId=$tokenId&hcpNihii=$nihii1&hcpSsin=$ssin1&hcpName=$name1&hcpQuality=${"doctor"}&passPhrase={passPhrase}",
                                                HttpMethod.GET, HttpEntity<Void>(createHeaders("0c381380-88fa-76da-24b7-0f99250031d6", "T@kt1k1Cur3")), String::class.java, passPhrase)
        Assertions.assertThat(genIns != null)
    }

    @Test
    fun getGeneralInsurability2() {
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)
        val genIns = this.restTemplate.getForObject("http://localhost:$port/genins/${"74010414733"}?keystoreId=$keystoreId&tokenId=$tokenId&hcpNihii=$nihii1&hcpSsin=$ssin1&hcpName=$name1&hcpQuality=${"doctor"}&passPhrase={passPhrase}", String::class.java, passPhrase)
        Assertions.assertThat(genIns != null)
    }

    @Test
    fun OneDayScenario01(){//ok partout
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)


        val genIns = getNisses(0).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii1" +
                "&hcpSsin=$ssin1" +
                "&hcpName=$name1" +
                "&date=${Instant.parse("2017-01-15T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"doctor"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }

        genIns.map{
            Assertions.assertThat(it.paymentByIo).isFalse()
        }

    }

    @Test
    fun OneDayScenario02(){//ok sauf 600
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)


        val genIns = getNisses(1).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii1" +
                "&hcpSsin=$ssin1" +
                "&hcpName=$name1" +
                "&date=${Instant.parse("2017-01-15T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"doctor"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.paymentByIo).isTrue()
        }

    }

    @Test
    fun OneDayScenario03(){//ok
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)


        val genIns = getNisses(2).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii1" +
                "&hcpSsin=$ssin1" +
                "&hcpName=$name1" +
                "&date=${Instant.parse("2017-01-15T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"doctor"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).contains("closedBefore")
        }

    }

    @Test
    fun OneDayScenario04(){//ok
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)


        val genIns = getNisses(3).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii1" +
                "&hcpSsin=$ssin1" +
                "&hcpName=$name1" +
                "&date=${Instant.parse("2017-01-15T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"doctor"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).contains("startLater")
        }

    }

    @Test
    fun OneDayScenario05(){//ok
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)

        val io = listOf<String>("126","305","526","615","910")
        var index=0;

        val genIns = getNisses(4).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/${io[index++]}/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii1" +
                "&hcpSsin=$ssin1" +
                "&hcpName=$name1" +
                "&date=${Instant.parse("2017-01-15T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"doctor"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it).isNotNull()
        }

    }

    @Test
    fun OneDayScenario06(){//ok
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)


        val genIns = getNisses(5).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii1" +
                "&hcpSsin=$ssin1" +
                "&hcpName=$name1" +
                "&date=${Instant.parse("2017-01-15T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"doctor"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.insurabilities[0].period!!.periodStart).isEqualTo(20170115)
        }

    }

    @Test
    fun OneDayScenario07(){//ok sauf 300
        val (keystoreId, tokenId, passPhrase) = register(restTemplate!!, port, ssin1!!, password1!!)


        val genIns = getNisses(6).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii1" +
                "&hcpSsin=$ssin1" +
                "&hcpName=$name1" +
                "&date=${Instant.parse("2017-01-15T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"doctor"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.medicalHouseInfo).isNotNull()
        }

    }

    //test with medicale house

    @Test
    fun PeriodScenario01(){//ok pour tous sauf le 600
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(7).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).isNull();
            Assertions.assertThat(it.paymentByIo).isTrue();
        }

    }

    @Test
    fun PeriodScenario02(){//ok
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(8).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).isNull();
            Assertions.assertThat(it.paymentByIo).isFalse();
        }

    }

    @Test
    fun PeriodScenario03(){//ok sauf 600
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(9).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).isNull();
            Assertions.assertThat(it.paymentByIo).isTrue();
        }

    }


    @Test
    fun PeriodScenario04(){//tous deceder mais que le 300 dans la periode
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(10).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).isNull();
            Assertions.assertThat(it.deceased).isNotNull();
        }

    }

    @Test
    fun PeriodScenario05(){//ok
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(11).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.insurabilities.size).isGreaterThan(1);
            Assertions.assertThat(it.insurabilities[0].ct1+"/"+it.insurabilities[0].ct2).isNotEqualTo(it.insurabilities[1].ct1+"/"+it.insurabilities[1].ct2);
        }

    }

    @Test
    fun PeriodScenario06(){//ok sauf 500
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(12).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.insurabilities.size).isGreaterThan(3);
        }

    }

    @Test
    fun PeriodScenario07(){//ok sauf 600 (down)
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(13).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).contains("startLater");
        }

    }

    @Test
    fun PeriodScenario08(){//ok
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(14).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).contains("closedBefore");
        }

    }

    @Test
    fun PeriodScenario11(){//ok
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(15).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.generalSituation).contains("changeDuring");
        }

    }

    @Test
    fun PeriodScenario12(){//ok sauf 500 600
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(16).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.insurabilities.size).isGreaterThanOrEqualTo(5);
        }

    }

    @Test
    fun PeriodScenario13(){//ok sauf 100 300
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)

        val io = listOf<String>("121","319","509","609","930")
        var index=0;

        val genIns = getNisses(17).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/${io[index++]}/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.faultMessage).isNull();
        }

    }

    @Test
    fun PeriodScenario14(){//ok
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(18).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            it.insurabilities.map{
                Assertions.assertThat(it.period!!.periodStart).isGreaterThanOrEqualTo(20160101).isLessThanOrEqualTo(20161231)
            }

        }

    }

    @Test
    fun PeriodScenario15(){//ok sauf 500 600
        val (keystoreId, tokenId, passPhrase) = registerMmH(restTemplate!!, port, nihii8_3!!, password3!!)


        val genIns = getNisses(19).map{
            this.restTemplate.getForObject("http://localhost:$port/genins/$it?keystoreId=$keystoreId"+
                "&tokenId=$tokenId"+
                "&hcpNihii=$nihii3" +
                "&hcpSsin=$ssin3" +
                "&hcpName=$name3" +
                "&date=${Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli()}"+
                "&endDate=${Instant.parse("2016-12-31T00:00:00.00Z").toEpochMilli()}"+
                "&hcpQuality=${"medicalhouse"}" +
                "&passPhrase={passPhrase}", InsurabilityInfoDto::class.java, passPhrase)

        }


        genIns.map{
            Assertions.assertThat(it.insurabilities.size).isEqualTo(1)
        }

    }
}
