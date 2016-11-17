/* ---------------------------------------------------------------------------------
 * CIE Color Space Conversion Class
 * This calss has methods that can convert RGB values to Luv
 * It also contains methods to get the LUV indexed color(quantized into 159 colors) 
 * based on programs by:
 *    Greg Thoenen and the University of Utah,
 *    Mohan Kankahalli, NUS
*/

public class cieConvert {

	public double L, u, v, r, g, b, Index;
	public LUVClass LuvIndex[] = new LUVClass[160];
	double X, Y, Z, uPrime, vPrime, un, vn;
	boolean errflag = false;

	{
		for (int i = 0; i < 160; i++) {     //creates an instance for all the LuvIndices
			LuvIndex[i] = new LUVClass();
		}
	}

	public void initLuvIndex() {
//initializes the LuvIndex
//formerly a file named. "Luv.dat"

		LuvIndex[0].L = 0.0;
		LuvIndex[0].u = 0.0;
		LuvIndex[0].v = 0.0;
		LuvIndex[1].L = 11.111111;
		LuvIndex[1].u = -14.363326;
		LuvIndex[1].v = 5.685223;
		LuvIndex[2].L = 11.111111;
		LuvIndex[2].u = 24.831306;
		LuvIndex[2].v = 5.685223;
		LuvIndex[3].L = 22.222222;
		LuvIndex[3].u = -14.363326;
		LuvIndex[3].v = -52.209036;
		LuvIndex[4].L = 22.222222;
		LuvIndex[4].u = -14.363326;
		LuvIndex[4].v = -23.261906;
		LuvIndex[5].L = 22.222222;
		LuvIndex[5].u = -14.363326;
		LuvIndex[5].v = 5.685223;
		LuvIndex[6].L = 22.222222;
		LuvIndex[6].u = 24.831306;
		LuvIndex[6].v = -23.261906;
		LuvIndex[7].L = 22.222222;
		LuvIndex[7].u = 24.831306;
		LuvIndex[7].v = 5.685223;
		LuvIndex[8].L = 22.222222;
		LuvIndex[8].u = 64.025939;
		LuvIndex[8].v = 5.685223;
		LuvIndex[9].L = 33.333333;
		LuvIndex[9].u = -14.363326;
		LuvIndex[9].v = -81.156165;
		LuvIndex[10].L = 33.333333;
		LuvIndex[10].u = -14.363326;
		LuvIndex[10].v = -52.209036;
		LuvIndex[11].L = 33.333333;
		LuvIndex[11].u = -14.363326;
		LuvIndex[11].v = -23.261906;
		LuvIndex[12].L = 33.333333;
		LuvIndex[12].u = -14.363326;
		LuvIndex[12].v = 5.685223;
		LuvIndex[13].L = 33.333333;
		LuvIndex[13].u = -14.363326;
		LuvIndex[13].v = 34.632352;
		LuvIndex[14].L = 33.333333;
		LuvIndex[14].u = 24.831306;
		LuvIndex[14].v = -52.209036;
		LuvIndex[15].L = 33.333333;
		LuvIndex[15].u = 24.831306;
		LuvIndex[15].v = -23.261906;
		LuvIndex[16].L = 33.333333;
		LuvIndex[16].u = 24.831306;
		LuvIndex[16].v = 5.685223;
		LuvIndex[17].L = 33.333333;
		LuvIndex[17].u = 24.831306;
		LuvIndex[17].v = 34.632352;
		LuvIndex[18].L = 33.333333;
		LuvIndex[18].u = 64.025939;
		LuvIndex[18].v = -23.261906;
		LuvIndex[19].L = 33.333333;
		LuvIndex[19].u = 64.025939;
		LuvIndex[19].v = 5.685223;
		LuvIndex[20].L = 33.333333;
		LuvIndex[20].u = 64.025939;
		LuvIndex[20].v = 34.632352;
		LuvIndex[21].L = 44.444444;
		LuvIndex[21].u = -53.557959;
		LuvIndex[21].v = -23.261906;
		LuvIndex[22].L = 44.444444;
		LuvIndex[22].u = -53.557959;
		LuvIndex[22].v = 5.685223;
		LuvIndex[23].L = 44.444444;
		LuvIndex[23].u = -53.557959;
		LuvIndex[23].v = 34.632352;
		LuvIndex[24].L = 44.444444;
		LuvIndex[24].u = -53.557959;
		LuvIndex[24].v = 63.579482;
		LuvIndex[25].L = 44.444444;
		LuvIndex[25].u = -14.363326;
		LuvIndex[25].v = -110.103295;
		LuvIndex[26].L = 44.444444;
		LuvIndex[26].u = -14.363326;
		LuvIndex[26].v = -81.156165;
		LuvIndex[27].L = 44.444444;
		LuvIndex[27].u = -14.363326;
		LuvIndex[27].v = -52.209036;
		LuvIndex[28].L = 44.444444;
		LuvIndex[28].u = -14.363326;
		LuvIndex[28].v = -23.261906;
		LuvIndex[29].L = 44.444444;
		LuvIndex[29].u = -14.363326;
		LuvIndex[29].v = 5.685223;
		LuvIndex[30].L = 44.444444;
		LuvIndex[30].u = -14.363326;
		LuvIndex[30].v = 34.632352;
		LuvIndex[31].L = 44.444444;
		LuvIndex[31].u = 24.831306;
		LuvIndex[31].v = -81.156165;
		LuvIndex[32].L = 44.444444;
		LuvIndex[32].u = 24.831306;
		LuvIndex[32].v = -52.209036;
		LuvIndex[33].L = 44.444444;
		LuvIndex[33].u = 24.831306;
		LuvIndex[33].v = -23.261906;
		LuvIndex[34].L = 44.444444;
		LuvIndex[34].u = 24.831306;
		LuvIndex[34].v = 5.685223;
		LuvIndex[35].L = 44.444444;
		LuvIndex[35].u = 24.831306;
		LuvIndex[35].v = 34.632352;
		LuvIndex[36].L = 44.444444;
		LuvIndex[36].u = 64.025939;
		LuvIndex[36].v = -52.209036;
		LuvIndex[37].L = 44.444444;
		LuvIndex[37].u = 64.025939;
		LuvIndex[37].v = -23.261906;
		LuvIndex[38].L = 44.444444;
		LuvIndex[38].u = 64.025939;
		LuvIndex[38].v = 5.685223;
		LuvIndex[39].L = 44.444444;
		LuvIndex[39].u = 64.025939;
		LuvIndex[39].v = 34.632352;
		LuvIndex[40].L = 44.444444;
		LuvIndex[40].u = 103.220572;
		LuvIndex[40].v = 5.685223;
		LuvIndex[41].L = 44.444444;
		LuvIndex[41].u = 103.220572;
		LuvIndex[41].v = 34.632352;
		LuvIndex[42].L = 44.444444;
		LuvIndex[42].u = 142.415204;
		LuvIndex[42].v = 34.632352;
		LuvIndex[43].L = 55.555556;
		LuvIndex[43].u = -53.557959;
		LuvIndex[43].v = -81.156165;
		LuvIndex[44].L = 55.555556;
		LuvIndex[44].u = -53.557959;
		LuvIndex[44].v = -52.209036;
		LuvIndex[45].L = 55.555556;
		LuvIndex[45].u = -53.557959;
		LuvIndex[45].v = -23.261906;
		LuvIndex[46].L = 55.555556;
		LuvIndex[46].u = -53.557959;
		LuvIndex[46].v = 5.685223;
		LuvIndex[47].L = 55.555556;
		LuvIndex[47].u = -53.557959;
		LuvIndex[47].v = 34.632352;
		LuvIndex[48].L = 55.555556;
		LuvIndex[48].u = -53.557959;
		LuvIndex[48].v = 63.579482;
		LuvIndex[49].L = 55.555556;
		LuvIndex[49].u = -14.363326;
		LuvIndex[49].v = -110.103295;
		LuvIndex[50].L = 55.555556;
		LuvIndex[50].u = -14.363326;
		LuvIndex[50].v = -81.156165;
		LuvIndex[51].L = 55.555556;
		LuvIndex[51].u = -14.363326;
		LuvIndex[51].v = -52.209036;
		LuvIndex[52].L = 55.555556;
		LuvIndex[52].u = -14.363326;
		LuvIndex[52].v = -23.261906;
		LuvIndex[53].L = 55.555556;
		LuvIndex[53].u = -14.363326;
		LuvIndex[53].v = 5.685223;
		LuvIndex[54].L = 55.555556;
		LuvIndex[54].u = -14.363326;
		LuvIndex[54].v = 34.632352;
		LuvIndex[55].L = 55.555556;
		LuvIndex[55].u = -14.363326;
		LuvIndex[55].v = 63.579482;
		LuvIndex[56].L = 55.555556;
		LuvIndex[56].u = 24.831306;
		LuvIndex[56].v = -110.103295;
		LuvIndex[57].L = 55.555556;
		LuvIndex[57].u = 24.831306;
		LuvIndex[57].v = -81.156165;
		LuvIndex[58].L = 55.555556;
		LuvIndex[58].u = 24.831306;
		LuvIndex[58].v = -52.209036;
		LuvIndex[59].L = 55.555556;
		LuvIndex[59].u = 24.831306;
		LuvIndex[59].v = -23.261906;
		LuvIndex[60].L = 55.555556;
		LuvIndex[60].u = 24.831306;
		LuvIndex[60].v = 5.685223;
		LuvIndex[61].L = 55.555556;
		LuvIndex[61].u = 24.831306;
		LuvIndex[61].v = 34.632352;
		LuvIndex[62].L = 55.555556;
		LuvIndex[62].u = 24.831306;
		LuvIndex[62].v = 63.579482;
		LuvIndex[63].L = 55.555556;
		LuvIndex[63].u = 64.025939;
		LuvIndex[63].v = -81.156165;
		LuvIndex[64].L = 55.555556;
		LuvIndex[64].u = 64.025939;
		LuvIndex[64].v = -52.209036;
		LuvIndex[65].L = 55.555556;
		LuvIndex[65].u = 64.025939;
		LuvIndex[65].v = -23.261906;
		LuvIndex[66].L = 55.555556;
		LuvIndex[66].u = 64.025939;
		LuvIndex[66].v = 5.685223;
		LuvIndex[67].L = 55.555556;
		LuvIndex[67].u = 64.025939;
		LuvIndex[67].v = 34.632352;
		LuvIndex[68].L = 55.555556;
		LuvIndex[68].u = 64.025939;
		LuvIndex[68].v = 63.579482;
		LuvIndex[69].L = 55.555556;
		LuvIndex[69].u = 103.220572;
		LuvIndex[69].v = -23.261906;
		LuvIndex[70].L = 55.555556;
		LuvIndex[70].u = 103.220572;
		LuvIndex[70].v = 5.685223;
		LuvIndex[71].L = 55.555556;
		LuvIndex[71].u = 103.220572;
		LuvIndex[71].v = 34.632352;
		LuvIndex[72].L = 55.555556;
		LuvIndex[72].u = 142.415204;
		LuvIndex[72].v = 5.685223;
		LuvIndex[73].L = 55.555556;
		LuvIndex[73].u = 142.415204;
		LuvIndex[73].v = 34.632352;
		LuvIndex[74].L = 55.555556;
		LuvIndex[74].u = 181.609837;
		LuvIndex[74].v = 34.632352;
		LuvIndex[75].L = 66.666667;
		LuvIndex[75].u = -92.752592;
		LuvIndex[75].v = 34.632352;
		LuvIndex[76].L = 66.666667;
		LuvIndex[76].u = -92.752592;
		LuvIndex[76].v = 63.579482;
		LuvIndex[77].L = 66.666667;
		LuvIndex[77].u = -92.752592;
		LuvIndex[77].v = 92.526611;
		LuvIndex[78].L = 66.666667;
		LuvIndex[78].u = -53.557959;
		LuvIndex[78].v = -81.156165;
		LuvIndex[79].L = 66.666667;
		LuvIndex[79].u = -53.557959;
		LuvIndex[79].v = -52.209036;
		LuvIndex[80].L = 66.666667;
		LuvIndex[80].u = -53.557959;
		LuvIndex[80].v = -23.261906;
		LuvIndex[81].L = 66.666667;
		LuvIndex[81].u = -53.557959;
		LuvIndex[81].v = 5.685223;
		LuvIndex[82].L = 66.666667;
		LuvIndex[82].u = -53.557959;
		LuvIndex[82].v = 34.632352;
		LuvIndex[83].L = 66.666667;
		LuvIndex[83].u = -53.557959;
		LuvIndex[83].v = 63.579482;
		LuvIndex[84].L = 66.666667;
		LuvIndex[84].u = -53.557959;
		LuvIndex[84].v = 92.526611;
		LuvIndex[85].L = 66.666667;
		LuvIndex[85].u = -14.363326;
		LuvIndex[85].v = -81.156165;
		LuvIndex[86].L = 66.666667;
		LuvIndex[86].u = -14.363326;
		LuvIndex[86].v = -52.209036;
		LuvIndex[87].L = 66.666667;
		LuvIndex[87].u = -14.363326;
		LuvIndex[87].v = -23.261906;
		LuvIndex[88].L = 66.666667;
		LuvIndex[88].u = -14.363326;
		LuvIndex[88].v = 5.685223;
		LuvIndex[89].L = 66.666667;
		LuvIndex[89].u = -14.363326;
		LuvIndex[89].v = 34.632352;
		LuvIndex[90].L = 66.666667;
		LuvIndex[90].u = -14.363326;
		LuvIndex[90].v = 63.579482;
		LuvIndex[91].L = 66.666667;
		LuvIndex[91].u = 24.831306;
		LuvIndex[91].v = -81.156165;
		LuvIndex[92].L = 66.666667;
		LuvIndex[92].u = 24.831306;
		LuvIndex[92].v = -52.209036;
		LuvIndex[93].L = 66.666667;
		LuvIndex[93].u = 24.831306;
		LuvIndex[93].v = -23.261906;
		LuvIndex[94].L = 66.666667;
		LuvIndex[94].u = 24.831306;
		LuvIndex[94].v = 5.685223;
		LuvIndex[95].L = 66.666667;
		LuvIndex[95].u = 24.831306;
		LuvIndex[95].v = 34.632352;
		LuvIndex[96].L = 66.666667;
		LuvIndex[96].u = 24.831306;
		LuvIndex[96].v = 63.579482;
		LuvIndex[97].L = 66.666667;
		LuvIndex[97].u = 64.025939;
		LuvIndex[97].v = -81.156165;
		LuvIndex[98].L = 66.666667;
		LuvIndex[98].u = 64.025939;
		LuvIndex[98].v = -52.209036;
		LuvIndex[99].L = 66.666667;
		LuvIndex[99].u = 64.025939;
		LuvIndex[99].v = -23.261906;
		LuvIndex[100].L = 66.666667;
		LuvIndex[100].u = 64.025939;
		LuvIndex[100].v = 5.685223;
		LuvIndex[101].L = 66.666667;
		LuvIndex[101].u = 64.025939;
		LuvIndex[101].v = 34.632352;
		LuvIndex[102].L = 66.666667;
		LuvIndex[102].u = 64.025939;
		LuvIndex[102].v = 63.579482;
		LuvIndex[103].L = 66.666667;
		LuvIndex[103].u = 103.220572;
		LuvIndex[103].v = -52.209036;
		LuvIndex[104].L = 66.666667;
		LuvIndex[104].u = 103.220572;
		LuvIndex[104].v = -23.261906;
		LuvIndex[105].L = 66.666667;
		LuvIndex[105].u = 103.220572;
		LuvIndex[105].v = 5.685223;
		LuvIndex[106].L = 66.666667;
		LuvIndex[106].u = 103.220572;
		LuvIndex[106].v = 34.632352;
		LuvIndex[107].L = 66.666667;
		LuvIndex[107].u = 103.220572;
		LuvIndex[107].v = 63.579482;
		LuvIndex[108].L = 66.666667;
		LuvIndex[108].u = 142.415204;
		LuvIndex[108].v = -23.261906;
		LuvIndex[109].L = 66.666667;
		LuvIndex[109].u = 142.415204;
		LuvIndex[109].v = 5.685223;
		LuvIndex[110].L = 66.666667;
		LuvIndex[110].u = 142.415204;
		LuvIndex[110].v = 34.632352;
		LuvIndex[111].L = 66.666667;
		LuvIndex[111].u = 142.415204;
		LuvIndex[111].v = 63.579482;
		LuvIndex[112].L = 66.666667;
		LuvIndex[112].u = 181.609837;
		LuvIndex[112].v = 63.579482;
		LuvIndex[113].L = 77.777778;
		LuvIndex[113].u = -92.752592;
		LuvIndex[113].v = -52.209036;
		LuvIndex[114].L = 77.777778;
		LuvIndex[114].u = -92.752592;
		LuvIndex[114].v = -23.261906;
		LuvIndex[115].L = 77.777778;
		LuvIndex[115].u = -92.752592;
		LuvIndex[115].v = 5.685223;
		LuvIndex[116].L = 77.777778;
		LuvIndex[116].u = -92.752592;
		LuvIndex[116].v = 34.632352;
		LuvIndex[117].L = 77.777778;
		LuvIndex[117].u = -92.752592;
		LuvIndex[117].v = 63.579482;
		LuvIndex[118].L = 77.777778;
		LuvIndex[118].u = -92.752592;
		LuvIndex[118].v = 92.526611;
		LuvIndex[119].L = 77.777778;
		LuvIndex[119].u = -53.557959;
		LuvIndex[119].v = -52.209036;
		LuvIndex[120].L = 77.777778;
		LuvIndex[120].u = -53.557959;
		LuvIndex[120].v = -23.261906;
		LuvIndex[121].L = 77.777778;
		LuvIndex[121].u = -53.557959;
		LuvIndex[121].v = 5.685223;
		LuvIndex[122].L = 77.777778;
		LuvIndex[122].u = -53.557959;
		LuvIndex[122].v = 34.632352;
		LuvIndex[123].L = 77.777778;
		LuvIndex[123].u = -53.557959;
		LuvIndex[123].v = 63.579482;
		LuvIndex[124].L = 77.777778;
		LuvIndex[124].u = -53.557959;
		LuvIndex[124].v = 92.526611;
		LuvIndex[125].L = 77.777778;
		LuvIndex[125].u = -14.363326;
		LuvIndex[125].v = -52.209036;
		LuvIndex[126].L = 77.777778;
		LuvIndex[126].u = -14.363326;
		LuvIndex[126].v = -23.261906;
		LuvIndex[127].L = 77.777778;
		LuvIndex[127].u = -14.363326;
		LuvIndex[127].v = 5.685223;
		LuvIndex[128].L = 77.777778;
		LuvIndex[128].u = -14.363326;
		LuvIndex[128].v = 34.632352;
		LuvIndex[129].L = 77.777778;
		LuvIndex[129].u = -14.363326;
		LuvIndex[129].v = 63.579482;
		LuvIndex[130].L = 77.777778;
		LuvIndex[130].u = -14.363326;
		LuvIndex[130].v = 92.526611;
		LuvIndex[131].L = 77.777778;
		LuvIndex[131].u = 24.831306;
		LuvIndex[131].v = -52.209036;
		LuvIndex[132].L = 77.777778;
		LuvIndex[132].u = 24.831306;
		LuvIndex[132].v = -23.261906;
		LuvIndex[133].L = 77.777778;
		LuvIndex[133].u = 24.831306;
		LuvIndex[133].v = 5.685223;
		LuvIndex[134].L = 77.777778;
		LuvIndex[134].u = 24.831306;
		LuvIndex[134].v = 34.632352;
		LuvIndex[135].L = 77.777778;
		LuvIndex[135].u = 24.831306;
		LuvIndex[135].v = 63.579482;
		LuvIndex[136].L = 77.777778;
		LuvIndex[136].u = 24.831306;
		LuvIndex[136].v = 92.526611;
		LuvIndex[137].L = 77.777778;
		LuvIndex[137].u = 64.025939;
		LuvIndex[137].v = -52.209036;
		LuvIndex[138].L = 77.777778;
		LuvIndex[138].u = 64.025939;
		LuvIndex[138].v = -23.261906;
		LuvIndex[139].L = 77.777778;
		LuvIndex[139].u = 64.025939;
		LuvIndex[139].v = 5.685223;
		LuvIndex[140].L = 77.777778;
		LuvIndex[140].u = 64.025939;
		LuvIndex[140].v = 34.632352;
		LuvIndex[141].L = 77.777778;
		LuvIndex[141].u = 64.025939;
		LuvIndex[141].v = 63.579482;
		LuvIndex[142].L = 77.777778;
		LuvIndex[142].u = 64.025939;
		LuvIndex[142].v = 92.526611;
		LuvIndex[143].L = 77.777778;
		LuvIndex[143].u = 103.220572;
		LuvIndex[143].v = 63.579482;
		LuvIndex[144].L = 88.888889;
		LuvIndex[144].u = -53.557959;
		LuvIndex[144].v = -23.261906;
		LuvIndex[145].L = 88.888889;
		LuvIndex[145].u = -53.557959;
		LuvIndex[145].v = 5.685223;
		LuvIndex[146].L = 88.888889;
		LuvIndex[146].u = -53.557959;
		LuvIndex[146].v = 34.632352;
		LuvIndex[147].L = 88.888889;
		LuvIndex[147].u = -53.557959;
		LuvIndex[147].v = 63.579482;
		LuvIndex[148].L = 88.888889;
		LuvIndex[148].u = -14.363326;
		LuvIndex[148].v = -23.261906;
		LuvIndex[149].L = 88.888889;
		LuvIndex[149].u = -14.363326;
		LuvIndex[149].v = 5.685223;
		LuvIndex[150].L = 88.888889;
		LuvIndex[150].u = -14.363326;
		LuvIndex[150].v = 34.632352;
		LuvIndex[151].L = 88.888889;
		LuvIndex[151].u = -14.363326;
		LuvIndex[151].v = 63.579482;
		LuvIndex[152].L = 88.888889;
		LuvIndex[152].u = -14.363326;
		LuvIndex[152].v = 92.526611;
		LuvIndex[153].L = 88.888889;
		LuvIndex[153].u = 24.831306;
		LuvIndex[153].v = -23.261906;
		LuvIndex[154].L = 88.888889;
		LuvIndex[154].u = 24.831306;
		LuvIndex[154].v = 5.685223;
		LuvIndex[155].L = 88.888889;
		LuvIndex[155].u = 24.831306;
		LuvIndex[155].v = 34.632352;
		LuvIndex[156].L = 88.888889;
		LuvIndex[156].u = 24.831306;
		LuvIndex[156].v = 63.579482;
		LuvIndex[157].L = 88.888889;
		LuvIndex[157].u = 24.831306;
		LuvIndex[157].v = 92.526611;
		LuvIndex[158].L = 100;
		LuvIndex[158].u = 0;
		LuvIndex[158].v = 0;

	}

	public int IndexOf() {
		// returns the Luv index based on the L,u,v attributes of the class
		// quantizes the Luv into 159 colors (0-158)

		int nL = 0, nHi = 0;   // holds the curent Lindex
		int nLastSame = 0;
		initLuvIndex();

		// find the which index the L value falls

		while (nHi < 159 && L > LuvIndex[nHi].L)
			nHi++;

		if (L != LuvIndex[nHi].L)
			nHi--;

		while (nL < 159 && LuvIndex[nL].L != LuvIndex[nHi].L) nL++;


		for (int i = nL; i < nHi; i++) {
			if (u <= LuvIndex[i].u) {       // within u value range
				if (v <= LuvIndex[i].v)      // and within v value range
					return (i);
			}
		}

		return (nHi);  // return error - can't find index for thisLuv??

	}

	public void RGBtoXYZ() {
		// converts the RGB values to XYZ
		// a necessary step before computing LUV

		//un = 4*Xn/(Xn + 15*Yn + 3*Zn)
		//vn = 9*Yn/(Xn + 15*Yn + 3*Zn)
		//These are precomputed and placed

		X = 0.607 * r + 0.174 * g + 0.200 * b;
		Y = 0.299 * r + 0.587 * g + 0.114 * b;
		Z = 0.066 * g + 1.116 * b;

		//Xn = 0.980722 Yn = 1 Zn = 1.182254
		un = 0.2022217;
		vn = 0.4608889;

	}

	public double round(double value, int decimalPlace) {
// a simple round function, round up to decimalPlace

		double power_of_ten = 1;
		while (decimalPlace-- > 0)
			power_of_ten *= 10.0;
		return Math.round(value * power_of_ten)
				/ power_of_ten;
	}

	public void XYZtoLUV() {
// converts the XYZ components into Luv

		double temp = X + 15.0 * Y + 3.0 * Z;
		//it appears uMin = -134
		//uMax = 220
		//vMin = -140
		//vMax = 122

		//Set L
		try {
			if (Y > 0.008856)
				L = 116.0 * Math.pow(Y, 0.3333333) - 16.0;
			else
				L = 903.3 * Y;
		} catch (Exception e) {
		}

		//set u & v
		if (temp > 0.000001) {
			uPrime = 4.0 * X / temp;
			vPrime = 9.0 * Y / temp;
			u = 13.0 * L * (uPrime - un);
			v = 13.0 * L * (vPrime - vn);
		} else {
			u = 0.0;
			v = 0.0;
		}

		L = round(L, 6); //round to 6 decimal places
		u = round(u, 6);
		v = round(v, 6);
	}

	public void RGBtoLUV() {
		RGBtoXYZ();
		XYZtoLUV();
	}

	public void LUVtoXYZ() {
		//un = 4*Xn/(Xn + 15*Yn + 3*Zn)
		//vn = 9*Yn/(Xn + 15*Yn + 3*Zn)
		//These are precomputed and placed in appropriate if-stmt

		//recall L==c,u==i,v==e

		if (L == 0.0)  //easy case to check
		{
			X = Y = Z = 0.0;
			return;
		}


		//Xn = 0.980722 Yn = 1 Zn = 1.182254
		un = 0.2022217;
		vn = 0.4608889;

		uPrime = u / (13.0 * L) + un;
		vPrime = v / (13.0 * L) + vn;

		try {
			if (L < 7.9996248)
				Y = L / 903.3;
			else
				Y = Math.pow((L + 16.0) / 116.0, 3.0);
		} catch (Exception e) {
		}

		X = -(9.0 * Y * uPrime) / ((uPrime - 4.0) * vPrime - uPrime * vPrime);
		Z = (9.0 * Y - 15.0 * vPrime * Y - vPrime * X) / (3.0 * vPrime);

	}

	public void XYZtoRGB() {

		r = 1.910 * X - 0.532 * Y - 0.288 * Z;
		g = -0.985 * X + 1.999 * Y - 0.028 * Z;
		b = 0.058 * X - 0.118 * Y + 0.898 * Z;


	}

	public void LUVtoRGB() {
		LUVtoXYZ();
		XYZtoRGB();
	}

	public void setValues(double nr, double ng, double nb) {

//use this to set the values of the r,g,b components of the class
//note: RGB should be normalized to 0-1
		r = nr;
		g = ng;
		b = nb;
// this function automatically converts the rgb components to Luv
//simply access the L,u,v public attributes of the class
		RGBtoLUV();
	}

	private class LUVClass {     //used to store the Luv value range for every index
		public double L, u, v;
	}

	public double computeLUVMaxDiff(){
		double max = 0;
		double currMax = max;

		for(int i=0; i<LuvIndex.length; i++){

			for(int j=i; j<LuvIndex.length; j++){
                currMax = getColorDifference(i,j);

				if(currMax > max)
					max = currMax;
			}
		}

		return max;
	}


	public double getColorDifference(int i , int j){

        return Math.sqrt(Math.pow(LuvIndex[i].L-LuvIndex[j].L,2) +
                         Math.pow(LuvIndex[i].u-LuvIndex[j].u,2) +
                         Math.pow(LuvIndex[i].v-LuvIndex[j].v,2));
    }

	public double[][] getSimilarityMatrix(double p) {

        double tColor = p*computeLUVMaxDiff();
        double[][] simMatrix = new double[LuvIndex.length][LuvIndex.length];

        for (int i = 0; i < LuvIndex.length; i++) {
            for (int j = 0; j < LuvIndex.length; j++) {
                double diff = getColorDifference(i, j);

                if (diff > tColor)
                    simMatrix[i][j] = 0;
                else
                    simMatrix[i][j] = 1 - diff / tColor;
            }
        }

        return simMatrix;
    }
}
