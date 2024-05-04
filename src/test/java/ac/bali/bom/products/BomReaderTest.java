package ac.bali.bom.products;

import ac.bali.bom.bootstrap.ModelLayer;
import ac.bali.bom.bootstrap.model.ProductsModule;
import ac.bali.bom.bootstrap.Qi4jApplicationAssembler;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.polygene.api.structure.Application;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.usecase.Usecase;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.api.value.ValueBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SuppressWarnings("DataFlowIssue")
public class BomReaderTest
{

    private static BomReader underTest;
    private static Module module;

    @BeforeClass
    public static void setup() throws Exception
    {
        Qi4jApplicationAssembler app = new Qi4jApplicationAssembler("Bill of Material", "1.0", Application.Mode.test);
        app.initialize();
        app.start();
        module = app.application().findModule(ModelLayer.NAME, ProductsModule.NAME);
        underTest = module.findService(BomReader.class).get();
    }

    @Test
    public void readBomTest1() throws Exception
    {
        Usecase usecase = UsecaseBuilder.newUsecase("readBomTest1");
        try(UnitOfWork uow = module.unitOfWorkFactory().newUnitOfWork(usecase))
        {
            URL url = getClass().getClassLoader().getResource("colibri-3/RevC/colibri-3-RevC-bom.csv");
            File f = new File(url.toURI());
            Bom bom = underTest.load("colibri-3", "C", f);
            assertThat(bom.product().get(), equalTo("colibri-3"));
            assertThat(bom.revision().get(), equalTo("C"));

            List<BomItem> expected = expected1();
            List<BomItem> actual = bom.items().get();
            assertThat(actual.size(), equalTo(expected.size()));
            for (int i = 0; i < expected.size(); i++)
            {
                assertThat(actual.get(i), equalTo(expected.get(i)));
            }
        }
    }

    @Test
    public void readBomTest2() throws Exception
    {
        Usecase usecase = UsecaseBuilder.newUsecase("readBomTest2");
        try(UnitOfWork uow = module.unitOfWorkFactory().newUnitOfWork(usecase))
        {
            File f = new File("build/resources/test/gasautomat1/RevA/gasautomat1.csv");
            Bom bom = underTest.load("gasautomat1", "C", f);
            assertThat(bom.product().get(), equalTo("gasautomat1"));
            assertThat(bom.revision().get(), equalTo("C"));

            List<BomItem> expected = expected2();
            List<BomItem> actual = bom.items().get();
            assertThat(actual.size(), equalTo(expected.size()));
            for (int i = 0; i < expected.size(); i++)
            {
                assertThat(actual.get(i), equalTo(expected.get(i)));
            }
        }
    }

    private List<BomItem> expected1()
    {
        List<BomItem> result = new ArrayList<>();
        result.add(createBomItem("220pF","C10","Capacitor_SMD:C_0603_1608Metric","C1603","","","",""));
        result.add(createBomItem("22uF","C12","Capacitor_SMD:C_0805_2012Metric","C45783","","","",""));
        result.add(createBomItem("100pF/50V","C13","Capacitor_SMD:C_0603_1608Metric","C14858","","","",""));
        result.add(createBomItem("10nF/50V","C15","Capacitor_SMD:C_0603_1608Metric","C57112","","","",""));
        result.add(createBomItem("220uF/100V","C20","Capacitor_SMD:CP_Elec_18x17.5","C340711","","","",""));
        result.add(createBomItem("10uF","C6,C18,C1,C2,C9,C11,C16,C17","Capacitor_SMD:C_1206_3216Metric","C13585","","","",""));
        result.add(createBomItem("0.1uF","C7,C19,C3","Capacitor_SMD:C_0603_1608Metric","C14663","","","",""));
        result.add(createBomItem("100nF","C8,C14","Capacitor_SMD:C_0603_1608Metric","C14663","","","",""));
        result.add(createBomItem("B560-13-F","D1","Diode_SMD:D_SMC","C85100","","","",""));
        result.add(createBomItem("1N5819W","D2","Diode_SMD:D_SOD-123","C402219","","","",""));
        result.add(createBomItem("19-217/GHC-YR1S2/3T","D3,D5,D6","LED_SMD:LED_0603_1608Metric","C72043","","","",""));
        result.add(createBomItem("B370","D4","Diode_SMD:D_SMC","C134456","","","",""));
        result.add(createBomItem("WS2812B","D7","LED_SMD:LED_WS2812B_PLCC4_5.0x5.0mm_P3.2mm","C139127","","","",""));
        result.add(createBomItem("Fuse","F1","Fuse:Fuse_Littelfuse-NANO2-451_453","C136373","","","",""));
        result.add(createBomItem("MountingHole_Pad","H7,H9,H8","Link2Web:Colibri-standoff","C2916321","732-10909-1-ND","","",""));
        result.add(createBomItem("Screw_Terminal_01x04","IO1,IO2","TerminalBlock_Phoenix:TerminalBlock_Phoenix_MKDS-1,5-4_1x04_P5.00mm_Horizontal","C424682","","","",""));
        result.add(createBomItem("Screw_Terminal_01x02","J2","TerminalBlock_Phoenix:TerminalBlock_Phoenix_MKDS-1,5-2_1x02_P5.00mm_Horizontal","C395860","","","",""));
        result.add(createBomItem("4.7uH","L1","Inductor_SMD:L_1210_3225Metric","C83415","","","",""));
        result.add(createBomItem("2.2uH","L2","Inductor_SMD:L_Sunlord_MWSA0518_5.4x5.2mm","C408345","","","",""));
        result.add(createBomItem("22uH","L3","Link2Web:Inductor-Sunlord-MWSA1004S","C380055","","","",""));
        result.add(createBomItem("AO3401A","Q4,Q5,Q1,Q2","Package_TO_SOT_SMD:SOT-23","C700954","","","",""));
        result.add(createBomItem("BSS138","Q6,Q3","Package_TO_SOT_SMD:SOT-23","C426569","","","",""));
        result.add(createBomItem("33k","R11","Resistor_SMD:R_0603_1608Metric","C4216","","","",""));
        result.add(createBomItem("7k5","R13","Resistor_SMD:R_0603_1608Metric","C23234","","","",""));
        result.add(createBomItem("270R","R14","Resistor_SMD:R_0603_1608Metric","C22966","","","",""));
        result.add(createBomItem("47k","R16","Resistor_SMD:R_0603_1608Metric"," C25819","","","",""));
        result.add(createBomItem("6k8","R17","Resistor_SMD:R_0603_1608Metric","C23212","","","",""));
        result.add(createBomItem("3k3","R19","Resistor_SMD:R_0603_1608Metric","C22978","","","",""));
        result.add(createBomItem("10k","R20,R21,R1,R2,R4,R9,R12,R18","Resistor_SMD:R_0603_1608Metric","C25804","","","",""));
        result.add(createBomItem("1k","R22,R10,R15","Resistor_SMD:R_0603_1608Metric","C21190","","","",""));
        result.add(createBomItem("3k9","R25,R26,R27,R28,R29,R30","Resistor_SMD:R_0603_1608Metric","C23018","","CRCW12063K90FKEA","Vishay Dale",""));
        result.add(createBomItem("0R","R3","Resistor_SMD:R_2512_6332Metric","C270957","","","",""));
        result.add(createBomItem("100k","R5","Resistor_SMD:R_0603_1608Metric","C23254","","","",""));
        result.add(createBomItem("5k6","R6","Resistor_SMD:R_0603_1608Metric","C13167","","","",""));
        result.add(createBomItem("12k","R7","Resistor_SMD:R_0603_1608Metric","C22790","","","",""));
        result.add(createBomItem("220k","R8","Resistor_SMD:R_0603_1608Metric","C22961","","","",""));
        result.add(createBomItem("74LS138","U1","Package_SO:TSSOP-16_4.4x5mm_P0.65mm","C157527","","","",""));
        result.add(createBomItem("TPS61040DBV","U2","Package_TO_SOT_SMD:SOT-23-5"," C7722","","","",""));
        result.add(createBomItem("TPS563200","U3","Package_TO_SOT_SMD:SOT-23-6","C97253","","","",""));
        result.add(createBomItem("TPS54360DDA","U4","Package_SO:TI_SO-PowerPAD-8_ThermalVias","C44377","","","",""));
        result.add(createBomItem("TCA9548APWR","U5","Package_SO:TSSOP-24_4.4x7.8mm_P0.65mm","C130026","","","",""));
        result.add(createBomItem("M.2 Key E","X0","TE_2199119-4","C841659","","","","2199119-4"));
        result.add(createBomItem("M.2Key-E","X2,X1","Link2Web:TE_2199119-4","C841659","","","",""));
        return result;
    }

    private List<BomItem> expected2()
    {
        List<BomItem> result = new ArrayList<>();
        result.add(createBomItem("C1, C2, C37, C38, C129, C130","27pF","Capacitor_SMD:C_0402_1005Metric","490-17672-1-ND","","","Murata","GJM1555C1H270JB01D","","Digikey","6"));
        result.add(createBomItem("C3, C4, C22, C36, C48, C68, C69, C76-C79, C89, C90, C93-C96, C109, C110, C117-C120, C125, C126, C133-C136","10uF","Capacitor_SMD:C_0603_1608Metric","490-10516-2-ND","","","Murata","GRM188R6YA106MA73D","","Digikey","29"));
        result.add(createBomItem("C5-C10, C19-C21, C23, C31, C32","100n","Capacitor_SMD:C_0402_1005Metric","587-1451-2-ND","","","TAIYO YUDEN EUROPE GMBH","RM EMK105 B7104KV-F","","Digikey","12"));
        result.add(createBomItem("C11, C17, C24, C33, C60, C80, C97, C142","10uF/63V","Capacitor_SMD:C_1206_3216Metric","490-9970-2-ND","","","Murata","GRM32ER71J106KA12L","","Digikey","8"));
        result.add(createBomItem("C12, C18, C25, C34, C61, C81, C98, C143","470nF/100V","Capacitor_SMD:C_1206_3216Metric","445-5203-2-ND","","C13585","TDK","C2012X7S2A474K125AB","","Digikey","8"));
        result.add(createBomItem("C13, C26, C62, C82, C99","470nF/10V","Capacitor_SMD:C_0603_1608Metric","490-3264-2-ND","","","Murata","GRM155R61A474KE15D","","Digikey","5"));
        result.add(createBomItem("C14, C27, C63, C83, C100","2.2uF/16V","Capacitor_SMD:C_0603_1608Metric","445-7438-2-ND","","","TDK","C1608X6S1C225K080AC","","Digikey","5"));
        result.add(createBomItem("C15, C16, C64, C101, C121","220uF/10V","Capacitor_Tantalum_SMD:CP_EIA-7343-43_Kemet-X","718-1034-2-ND","Price","","Vishay-Sprague","593D227X0010E2TE3","","Digikey","5"));
        result.add(createBomItem("C28, C35, C84-C87, C138, C144","100nF","Capacitor_SMD:C_0603_1608Metric","490-16477-2-ND","","C14663","Murata","GCJ188R71E104KA12D","","Digikey","8"));
        result.add(createBomItem("C29, C30, C49, C50, C146, C147","22uF/50V","Capacitor_SMD:C_1206_3216Metric","445-1655-2-ND","Price","","TDK","CKG57NX5R1H226M500JH","","Digikey","6"));
        result.add(createBomItem("C39-C47, C51-C59","1uF/25V","Capacitor_SMD:C_0603_1608Metric","490-10673-2-ND","","C15849","Murata","GCM188R71E105KA64D","","Digikey","18"));
        result.add(createBomItem("C74, C75, C91, C92, C115, C116, C131, C132","100uF/16V","Capacitor_Tantalum_SMD:CP_EIA-7343-31_Kemet-D","399-9787-2-ND","","","KEMET","T521V107M016ATE050","","Digikey","8"));
        result.add(createBomItem("C88","10uF","Capacitor_SMD:C_1206_3216Metric","1276-2876-2-ND","","C13585","Samsung","CL31A106KBHNNNE","","Digikey","1"));
        result.add(createBomItem("C137","0.1uF","Capacitor_SMD:C_0402_1005Metric","490-6321-2-ND","","","Murata","GRM155R71A104KA01D","","Digikey","1"));
        result.add(createBomItem("C139","2.9uF","Capacitor_SMD:C_0805_2012Metric","1276-1052-2-ND","","","Samsung","CL21A106KPFNNNE","","Digikey","1"));
        result.add(createBomItem("C140","22uF","Capacitor_SMD:C_0805_2012Metric","490-1719-2-ND","","","Murata","GRM21BR60J226ME39L","","Digikey","1"));
        result.add(createBomItem("C141","33nF","Capacitor_SMD:C_0402_1005Metric","490-1315-2-ND","","","Murata","GRM155R71A333KA01D","","Digikey ","1"));
        result.add(createBomItem("D1","LED Green","LED_SMD:LED_0603_1608Metric","LTST-S270KGKT","","","Rohm","SML-A12M8TT86N","","Digikey ","1"));
        result.add(createBomItem("D2","LED Red","LED_SMD:LED_0603_1608Metric","LTST-S270KRKT","","","Rohm","SML-A12U8TT86Q","","Digikey ","1"));
        result.add(createBomItem("D3, D5, D7, D9, D11, D13, D15, D17, D19, D21, D23, D25, D27","Green","LED_SMD:LED_0603_1608Metric","160-1183-2-ND","","","Lite-On Inc.","LTST-C190GKT","","Digikey ","13"));
        result.add(createBomItem("D4, D6, D8, D10, D12, D14, D16, D18, D20, D22, D24, D26, D28","Blue","LED_SMD:LED_0603_1608Metric","160-1827-2-ND","","","Lite-On Inc.","LTST-C193TBKT-5A","","Digikey ","13"));
        result.add(createBomItem("D29-D36","WS2812B","LED_SMD:LED_WS2812B_PLCC4_5.0x5.0mm_P3.2mm","","","C114586","","","","LCSC","8"));
        result.add(createBomItem("D37-D59","ES2DA-13-F","Diode_SMD:D_SMA","ES2DA-FDITR-ND","","","Diodes Incorporated","ES2DA-13-F","","Digikey","23"));
        result.add(createBomItem("H1-H4","MountingHole","MountingHole:MountingHole_4.3mm_M4","","","","","","","NoMount","4"));
        result.add(createBomItem("H11-H17","MountingHole","MountingHole:MountingHole_3.2mm_M3_DIN965_Pad_TopOnly","","","","","","","NoMount","7"));
        result.add(createBomItem("IO1-IO8","Screw_Terminal_01x04","Connector_Phoenix_MSTB:PhoenixContact_MSTBA_2,5_4-G-5,08_1x04_P5.08mm_Horizontal","277-1152-ND","","","Phoenix Contact","1755752","","Digikey","8"));
        result.add(createBomItem("J1, J15-J17","690-008-221-904","CM4IO:MOLEX_USB_67298-4090","WM17130-ND","","","Molex","0672984090","","Digikey","4"));
        result.add(createBomItem("J2","Molex 470531000","Connector:FanPinHeader_1x04_P2.54mm_Vertical","WM4330-ND","","","Molex","0470531000","","Digikey","1"));
        result.add(createBomItem("J3","Conn_02x05_Odd_Even","Connector_PinHeader_2.54mm:PinHeader_2x05_P2.54mm_Vertical","609-5163-2-ND","","","Amphenol","95278-801A10LF","","Digikey","1"));
        result.add(createBomItem("J4-J6","Conn_01x03","Connector_Phoenix_MSTB:PhoenixContact_MSTBA_2,5_3-G-5,08_1x03_P5.08mm_Horizontal","277-6489-ND","","","Phoenix Contact","1924318","","Digikey","3"));
        result.add(createBomItem("J7, J11, J12, J14, J18-J26","MSTB-02","Connector_Phoenix_MSTB:PhoenixContact_MSTBA_2,5_2-G-5,08_1x02_P5.08mm_Horizontal","277-1106-ND","","","Phoenix Contact","1757242","","Digikey","13"));
        result.add(createBomItem("J8, J9","10029449111","IHLP:CONN_10029449111_AMP","609-4614-2-ND","","","Amphenol ICC (FCI)","10029449-111RLF","","Digikey","2"));
        result.add(createBomItem("J10","Conn_01x03","Connector_PinHeader_2.54mm:PinHeader_1x03_P2.54mm_Vertical","","","","","","","NoMount","1"));
        result.add(createBomItem("J13","Micro_SD_Card_Det","CM4IO:SDCARD_MOLEX_503398-1892","WM11190TR-ND","","","Molex","503398-1892","","Farnell","1"));
        result.add(createBomItem("J27","Screw_Terminal_01x04","Connector_Phoenix_MSTB:PhoenixContact_MSTBA_2,5_4-G-5,08_1x04_P5.08mm_Horizontal","277-1108-ND","","","Phoenix Contact","1757268","","Digikey","1"));
        result.add(createBomItem("L1-L5","8.2uH","IHLP:IND_IHLP-6767GZ_VIS-M","541-1258-2-ND","","","Vishay-Dale","IHLP6767GZER8R2M01","","Digikey","5"));
        result.add(createBomItem("L6","820nH","Inductor_SMD:L_Vishay_IHLP-1212","541-1318-2-ND","","","Vishay-Dale","IHLP1212BZERR88M11","","Digikey","1"));
        result.add(createBomItem("L7-L9","10uH","IHLP:IHLP5050EZER100M01","541-1030-2-ND","","","Vishay-Dale","IHLP5050EZER100M01","","Digikey","3"));
        result.add(createBomItem("Module1","ComputeModule4","CM4IO:Raspberry-Pi-4-Compute-Module","H11615TR-ND","","","Hirose","DF40C-100DS-0.4V","","Digikey","1"));
        result.add(createBomItem("Q1, Q4, Q7, Q10, Q13, Q14, Q17, Q20, Q23-Q27, Q44-Q46","2N7002","Package_TO_SOT_SMD:SOT-23","2N7002KT1GOSTR-ND","","C116584","ON Semi","2N7002KT1G","","Digikey","16"));
        result.add(createBomItem("Q2, Q5, Q8, Q11, Q15, Q18, Q21","NTR2101P","Package_TO_SOT_SMD:SOT-23","NTR2101PT1GOSTR-ND","","C168745","ON Semi","NTR2101PT1G","","Digikey","7"));
        result.add(createBomItem("Q3, Q6, Q9, Q12, Q16, Q19, Q22","NTR1P02T1G","Package_TO_SOT_SMD:SOT-23","NTR1P02T1GOSTR-ND","","C129192","ON Semi","NTR1P02T1G","","Digikey","7"));
        result.add(createBomItem("Q28-Q43","2N7002-7-F","Package_TO_SOT_SMD:SOT-23","2N7002KT1GOSTR-ND","","C116584","ON Semi","2N7002KT1G","","Digikey","16"));
        result.add(createBomItem("R1-R4, R6, R9, R143-R145","36K 1%","Resistor_SMD:R_0603_1608Metric","311-36.0KHRTR-ND","","","YAGEO","RC0603FR-0736KL","","Digikey","9"));
        result.add(createBomItem("R5, R19","12K 1%","Resistor_SMD:R_0603_1608Metric","311-12.0KHRTR-ND","","","YAGEO","RC0603FR-0712KL","","Digikey","2"));
        result.add(createBomItem("R7, R79, R146, R163, R172","0R","Resistor_SMD:R_0603_1608Metric","311-0.0HRTR-ND","","C25804","YAGEO","RC0603FR-070RL","","Digikey","5"));
        result.add(createBomItem("R8, R21, R22, R80, R81, R127-R142, R147, R148, R153, R164, R165, R176, R177, R194, R197, R200, R203, R206, R209, R212, R219-R222","10k","Resistor_SMD:R_0603_1608Metric","311-10.0KHRTR-ND","","C25804","YAGEO","RC0603FR-0710KL","","Digikey","39"));
        result.add(createBomItem("R10","9.09k","Resistor_SMD:R_0603_1608Metric","541-9.09KHTR-ND","","","Vishay-Dale","CRCW06039K09FKEA","","Digikey","1"));
        result.add(createBomItem("R11, R20, R159, R162, R171, R173-R175","15K","Resistor_SMD:R_0603_1608Metric","311-15.0KHRTR-ND","","","YAGEO","RC0603FR-0715KL","","Digikey","8"));
        result.add(createBomItem("R12, R14-R16, R40, R41, R44, R45, R48, R49, R52, R53, R56, R57, R60, R61, R63, R65, R85, R86, R89, R90, R93, R94, R97, R98, R101, R102, R105, R106, R180-R193, R195, R196, R198, R199, R201, R202, R204, R205, R207, R208, R210, R211, R213-R218","1k","Resistor_SMD:R_0603_1608Metric","311-1.00KHRTR-ND","","C21190","YAGEO","RC0603FR-071KL","","Digikey","62"));
        result.add(createBomItem("R13, R178, R223","422k","Resistor_SMD:R_0402_1005Metric","541-422KLTR-ND","","","Vishay-Dale","CRCW0402422KFKED","","Digikey","3"));
        result.add(createBomItem("R17, R18","470R","Resistor_SMD:R_0603_1608Metric","311-470GRTR-ND","","","YAGEO","RC0603JR-07470RL","","Digikey","2"));
        result.add(createBomItem("R23, R82, R149, R154, R166","133k","Resistor_SMD:R_0603_1608Metric","311-133KHRTR-ND","","","YAGEO","RC0603FR-07133KL","","Digikey","5"));
        result.add(createBomItem("R24, R25, R27-R36, R39, R42, R43, R46, R47, R50, R51, R54, R55, R58, R59, R62, R64, R66-R77, R83, R84, R87, R88, R91, R92, R95, R96, R99, R100, R103, R104, R107, R150, R151, R155, R156, R167, R168","100k","Resistor_SMD:R_0603_1608Metric","311-100KHRTR-ND","","C25803","YAGEO","RC0603FR-07100KL","","Digikey","56"));
        result.add(createBomItem("R26, R152, R157, R160, R169","24.9k","Resistor_SMD:R_0603_1608Metric","311-24.9KHRTR-ND","","C25803","YAGEO","RC0603FR-0724K9L","","Digikey","5"));
        result.add(createBomItem("R37, R108","1M","Resistor_SMD:R_0603_1608Metric","311-1.00MHRTR-ND","","C22935","YAGEO","RC0603FR-071ML","","Digikey","2"));
        result.add(createBomItem("R38, R78","12k","Resistor_SMD:R_0603_1608Metric","311-12KGRTR-ND","","C22790","YAGEO","RC0603JR-0712KL","","Digikey","2"));
        result.add(createBomItem("R109-R126","3k9","Resistor_SMD:R_0603_1608Metric","311-3.90KHRTR-ND","","C23018","YAGEO","RC0603FR-073K9L","","Digikey","18"));
        result.add(createBomItem("R158","30.9k","Resistor_SMD:R_0402_1005Metric","541-30.9KLTR-ND","","","Vishay-Dale","CRCW040230K9FKED","","Digikey","1"));
        result.add(createBomItem("R161","10k","Resistor_SMD:R_0402_1005Metric","541-10.0KLTR-ND","","","Vishay-Dale","CRCW040210K0FKED","","Digikey","1"));
        result.add(createBomItem("R170, R179, R224","22.1k","Resistor_SMD:R_0402_1005Metric","541-22.1KLTR-ND","","","Vishay-Dale","CRCW040222K1FKED","","Digikey","3"));
        result.add(createBomItem("RV1-RV16","Varistor 47V 300A","Resistor_SMD:R_1210_3225Metric","399-13766-2-ND","","","Kemet","VC1210K301R030","","Digikey","16"));
        result.add(createBomItem("SW1","SW_Push","Button_Switch_SMD:SW_SPST_PTS645","CKN12221-2-ND","","C2834922","C&K","PTS526 SK15 SMTR2 LFS","","Digikey","1"));
        result.add(createBomItem("TP2","TestPoint","TestPoint:TestPoint_Pad_2.0x2.0mm","","","","","","","","1"));
        result.add(createBomItem("U1","24LC64","Package_SO:SO-8_3.9x4.9mm_P1.27mm","CAT24C64YI-GT3OSTR-ND","","","ON Semi","CAT24C64YI-GT3","","Digikey","1"));
        result.add(createBomItem("U2","USB2514B-I/M2","Package_DFN_QFN:QFN-36-1EP_6x6mm_P0.5mm_EP3.7x3.7mm","USB2514B-I/M2-ND","","","Microchip","USB2514B-I/M2","","Digikey","1"));
        result.add(createBomItem("U3, U11, U14, U17, U34","LM76005","MyWQFN:Texas_WQFN-34_EP_4x6_Pitch0.5mm","296-LM76005RNPRTR-ND","","","Texas Instrument","LM76005RNPR","","Digikey","5"));
        result.add(createBomItem("U4, U15, U16, U18, U19, U32, U33, U35","AP22653W6","Package_TO_SOT_SMD:SOT-23-6","AP2553W6-7DICT-ND","","","Diodes","AP22653W6","","Digikey","8"));
        result.add(createBomItem("U5","EMC2301-1-ACZL-TR","Package_SO:SOIC-8_5.275x5.275mm_P1.27mm","EMC2301-1-ACZL-CT-ND","","","Microchip","EMC2301-1-ACZL-TR","","Digikey","1"));
        result.add(createBomItem("U6","RT9742SNGV","Package_TO_SOT_SMD:SOT-23","1028-RT9742SNGVTR-ND","","","RichTek","RT9742SNGV","","Digikey","1"));
        result.add(createBomItem("U7, U8, U29-U31","TPD4EUSB30","Package_SON:USON-10_2.5x1.0mm_P0.5mm","296-28063-2-ND","","","Texas Instrument","TPD4EUSB30DQAR","","Digikey","5"));
        result.add(createBomItem("U9","RT9742GGJ5","Package_TO_SOT_SMD:SOT-23-5","1028-1436-2-ND","","","RichTek","RT9742GGJ5","","Digikey","1"));
        result.add(createBomItem("U10","MagJack-A70-112-331N126","CM4IO:TRJG0926HENL","","","","Trxcom","TRJG0926HENL","","https://www.ecplaza.net/products/trjg0926henl-for-raspberry-pi-board-4678695","1"));
        result.add(createBomItem("U12, U13","USB2517","IHLP:USB2517I-JZX","638-1090-ND","","C626667","Microchip Technology","USB2517I-JZX","","Digikey","2"));
        result.add(createBomItem("U20","PCA9548APW","Package_SO:TSSOP-24_4.4x7.8mm_P0.65mm","296-33511-5-ND","","C21198","Texas Instrument","PCA9548APW","","Digikey","1"));
        result.add(createBomItem("U21","STM32F030C8Tx","Package_QFP:LQFP-48_7x7mm_P0.5mm","497-17331-2-ND","","C23922","STMicroelectronics","STM32F030C8T6TR","","Digikey","1"));
        result.add(createBomItem("U22-U28","M.2Key-E","Link2Web:TE_2199119-4","MDT420E01001TR-ND","","C841659","Amphenol ICC (FCI)","MDT420E01001","","Digikey","7"));
        result.add(createBomItem("U36","TPS62933F","Package_TO_SOT_SMD:SOT-583-8","296-TPS62933FDRLRTR-ND","","","Texas Instrument","TPS62933FDRLR","","Digikey","1"));
        result.add(createBomItem("U37-U39","LMR51430","Package_TO_SOT_SMD:SOT-23-6","296-LMR51430XDDCRTR-ND","","","Texas Instrument","LMR51430XDDCR","","Digikey","3"));
        result.add(createBomItem("U40-U48","AQY211G2S","Package_SO:SO-4_4.4x4.3mm_P2.54mm","255-5277-5-ND","","C129283","Panasonic","AQY211G2S","","Digikey","9"));
        result.add(createBomItem("U49","W25Q128JVS","Package_SO:SOIC-8_5.23x5.23mm_P1.27mm","","","","","","","","1"));
        result.add(createBomItem("Y1-Y3","24MHz","Crystal:Crystal_SMD_TXC_7M-4Pin_3.2x2.5mm","887-1130-2-ND","","","TXC CORPORATION","7M-24.000MAAJ-T","","Digikey","3"));
        return result;

    }
    private BomItem createBomItem(String designators, String value, String footprint, String digikey, String issue, String lcsc, String mf, String mpn, String mouser, String supplier, String quantity)
    {
        ValueBuilder<BomItem> builder = module.valueBuilderFactory().newValueBuilder(BomItem.class);
        BomItem prototype = builder.prototype();
        prototype.designator().set(designators);
        prototype.quantity().set(Integer.parseInt(quantity));
        prototype.mf().set(mf);
        prototype.mpn().set(mpn);
        prototype.footprint().set(footprint);
        prototype.value().set(value);
        return builder.newInstance();
    }

    private BomItem createBomItem(String value, String designators, String footprint, String lcsc, String digikey, String mpn, String mf, String comment)
    {
        ValueBuilder<BomItem> builder = module.valueBuilderFactory().newValueBuilder(BomItem.class);
        BomItem prototype = builder.prototype();
        prototype.quantity().set(BomReader.Mixin.countDesignators(designators));
        prototype.mf().set(mf);
        prototype.mpn().set(mpn);
        prototype.footprint().set(footprint);
        prototype.value().set(value);
        prototype.designator().set(designators);
        return builder.newInstance();
    }
}
