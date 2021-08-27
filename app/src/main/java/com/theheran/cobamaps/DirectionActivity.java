package com.theheran.cobamaps;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String API_KEY = "AIzaSyCFZ9FOShGpoxHFinBkZ4DsrjiawbuHn4w";
    private LatLng pickUpLatLng = new LatLng(-6.175110, 106.865039);
    private LatLng locationLatLng = new LatLng(-6.197301,106.795951);

    private TextView tvStartAddress, tvEndAddress, tvDuration, tvDistance;
    private LinearLayout panelInfo;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        // Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        // Set Title bar
        getSupportActionBar().setTitle("Testing Direction Maps");

        tvStartAddress = findViewById(R.id.tvStartAddress);
        tvEndAddress = findViewById(R.id.tvEndAddress);
        tvDuration = findViewById(R.id.tvDuration);
        tvDistance = findViewById(R.id.tvDistance);
        panelInfo = findViewById(R.id.panelInfo);

        handler = new Handler();

        GetDirection();
    }



    private void GetDirection(){
        try{
            mMap.clear();
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        String lokasiAwal = pickUpLatLng.latitude + "," + pickUpLatLng.longitude;
                        String lokasiAkhir = locationLatLng.latitude + "," + locationLatLng.longitude;
                        String strResult= new SyncDirection().execute(lokasiAwal, lokasiAkhir, API_KEY).get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            }; thread.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class SyncDirection extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String urlConnect = "https://maps.googleapis.com/maps/api/directions/json";
            String queryParam = "origin="+strings[0]+"&destination="+strings[1]+"&key="+strings[2]+"";
            urlConnect+="?"+queryParam;
            String response = httpHandler.callService(urlConnect,
                    Integer.parseInt(getResources().getString(R.string.json_timeout))*1000);

            response = "{\n" +
                    "  \"geocoded_waypoints\": [\n" +
                    "    {\n" +
                    "      \"geocoder_status\": \"OK\",\n" +
                    "      \"place_id\": \"ChIJu1tunmTibi4R_hL-pzh2oHA\",\n" +
                    "      \"types\": [\n" +
                    "        \"locality\",\n" +
                    "        \"political\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"geocoder_status\": \"OK\",\n" +
                    "      \"place_id\": \"ChIJnUvjRenzaS4RoobX2g-_cVM\",\n" +
                    "      \"types\": [\n" +
                    "        \"colloquial_area\",\n" +
                    "        \"locality\",\n" +
                    "        \"political\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"routes\": [\n" +
                    "    {\n" +
                    "      \"bounds\": {\n" +
                    "        \"northeast\": {\n" +
                    "          \"lat\": -6.1921366,\n" +
                    "          \"lng\": 108.5534377\n" +
                    "        },\n" +
                    "        \"southwest\": {\n" +
                    "          \"lat\": -6.7673916,\n" +
                    "          \"lng\": 106.8455976\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"copyrights\": \"Map data ©2021\",\n" +
                    "      \"legs\": [\n" +
                    "        {\n" +
                    "          \"distance\": {\n" +
                    "            \"text\": \"220 km\",\n" +
                    "            \"value\": 220082\n" +
                    "          },\n" +
                    "          \"duration\": {\n" +
                    "            \"text\": \"3 hours 2 mins\",\n" +
                    "            \"value\": 10914\n" +
                    "          },\n" +
                    "          \"end_address\": \"Jakarta, Indonesia\",\n" +
                    "          \"end_location\": {\n" +
                    "            \"lat\": -6.2087663,\n" +
                    "            \"lng\": 106.8455976\n" +
                    "          },\n" +
                    "          \"start_address\": \"Cirebon, Cirebon City, West Java, Indonesia\",\n" +
                    "          \"start_location\": {\n" +
                    "            \"lat\": -6.7322704,\n" +
                    "            \"lng\": 108.5526226\n" +
                    "          },\n" +
                    "          \"steps\": [\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"78 m\",\n" +
                    "                \"value\": 78\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 40\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.732434899999999,\n" +
                    "                \"lng\": 108.5533056\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Head <b>east</b> toward <b>Jl. Cideng</b>\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"t{ah@{tpuS^iC\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.7322704,\n" +
                    "                \"lng\": 108.5526226\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"38 m\",\n" +
                    "                \"value\": 38\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 15\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.732635999999999,\n" +
                    "                \"lng\": 108.5530328\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Turn <b>right</b> onto <b>Gg. Sijarak</b>\",\n" +
                    "              \"maneuver\": \"turn-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"t|ah@eypuS@@HHDDVd@\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.732434899999999,\n" +
                    "                \"lng\": 108.5533056\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"89 m\",\n" +
                    "                \"value\": 89\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 30\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.7333307,\n" +
                    "                \"lng\": 108.5534377\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Turn <b>left</b> toward <b>Jl. Kesambi</b>\",\n" +
                    "              \"maneuver\": \"turn-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"~}ah@mwpuSp@]B?BABALGLIn@]\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.732635999999999,\n" +
                    "                \"lng\": 108.5530328\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.9 km\",\n" +
                    "                \"value\": 912\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"3 mins\",\n" +
                    "                \"value\": 156\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.7395847,\n" +
                    "                \"lng\": 108.548197\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Turn <b>right</b> onto <b>Jl. Kesambi</b><div style=\\\"font-size:0.9em\\\">Pass by UNIVERSITAS CIC (on the left)</div>\",\n" +
                    "              \"maneuver\": \"turn-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"hbbh@_zpuSHRb@p@TXJLHHv@r@PPXRn@d@BBNJ@@b@\\\\PNBBPLZXBDJJHLj@h@RR`@d@h@v@?@`@b@PR~@j@TPLFpBfA^RDBh@Z~A~@XPTLLFZRB@v@f@DBxBpA\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.7333307,\n" +
                    "                \"lng\": 108.5534377\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"4.0 km\",\n" +
                    "                \"value\": 3978\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"8 mins\",\n" +
                    "                \"value\": 498\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.7663923,\n" +
                    "                \"lng\": 108.5256749\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"At Sultan Print, continue onto <b>Jl. Jend. Sudirman</b>/<wbr/><b>Jl. Kanggraksan</b>/<wbr/><b>Jl. Raya Pantura</b><div style=\\\"font-size:0.9em\\\">Continue to follow Jl. Raya Pantura</div><div style=\\\"font-size:0.9em\\\">Pass by Alfamart (on the left in 3.1&nbsp;km)</div>\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"jich@gyouSRNh@Zx@p@DBXV?@p@v@TXJJDDb@f@BDJTHLZn@JV\\\\`A@@HZHT\\\\x@@BRb@FLPXBBHHDDRRh@`@HFHFj@`@v@f@NHFDvAr@DBbAb@@@`AZRFJD@@n@\\\\xAt@fAj@LDFBj@XD@h@TTH`@NHB\\\\Lj@PB@dCv@ZJd@NdCr@PDPFjEbBdEfBHFFHHDjAf@j@XzB`ArD~AhAh@x@ZpC~@|DrAhCz@XNJFLHLLLRn@lABBNVn@lADFJN\\\\p@`@r@\\\\p@Xd@NX^l@JRJPZ\\\\|A~Ax@v@DDVT`@b@|AbBl@r@^`@ZZ^\\\\HLx@t@l@d@FF\\\\Xn@l@n@v@HFz@z@LHNNh@f@f@f@h@r@l@x@RTt@`ARV`@h@\\\\b@PTJL\\\\\\\\\\\\d@RXn@t@^f@xAjBrAbBnBfCn@n@PR\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.7395847,\n" +
                    "                \"lng\": 108.548197\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"1.0 km\",\n" +
                    "                \"value\": 960\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"2 mins\",\n" +
                    "                \"value\": 107\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.7624508,\n" +
                    "                \"lng\": 108.5275979\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Take the ramp to <b>Bandung</b>/<wbr/><b>Jakarta</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"maneuver\": \"ramp-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"|phh@mlkuSr@VNBHBH@H?L?H?JCVE^[HIDGDIDK@G@I@O?KAKAKAGAECKEKEKIOOSY_@mAyAgAwAaB}Bu@aA[_@QQUSQMQISGSESCQ?M@A?MBMDMFYLQHWNgAz@WR{@t@OLcAz@_@Xu@`@\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.7663923,\n" +
                    "                \"lng\": 108.5256749\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"14.9 km\",\n" +
                    "                \"value\": 14872\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"11 mins\",\n" +
                    "                \"value\": 671\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.6898705,\n" +
                    "                \"lng\": 108.4300083\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Merge onto <b>Jl. Raya Pantura</b>/<wbr/><b>Jl. Tol Kanci - Palimanan</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"maneuver\": \"merge\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"hxgh@oxkuSYVOJIFWRkA|@aBnAuAbAcAp@yBtAiBjAgAl@c@V]PKFaEvBsAp@_@N]Pc@R_Bp@SHyB|@UJGBiC`AgBl@aBh@}Br@iBh@aBd@MBQFWHwH|BmCt@w@VMBA@A?w@TkBj@u@RkA\\\\gCt@a@LgCr@GBC@qDdA_Bd@oEpA]Jq^pKsAZkA\\\\yU|GA@s@T_AVaX`Ic@HyAb@w@TsF`B_FvAsDfAkBj@oBn@mBn@kA^YJEBa@LiA`@cA`@MFw@Z{Al@o@Z{Ar@m@Zq@ZcAl@gDjBuAz@oChBoA|@iCnBA@C@A@kB~A{@v@{@t@}@z@}@|@A@u@t@k@l@kApAQRCDSVGFc@h@a@f@aApAg@n@UZm@x@uApBKPMNg@t@o@`AYb@{@rAk@x@{A~Bu@fASZKNCDs@fAEFaAxA}G|J_HjKGJ[d@_AtAiBlCsCfEEFEDEFeCvD{BfDy@jAcCpDm@bAaAvAgD`Fy@nAYb@W`@q@bAcAzA{BdD_B`CyAfCc@r@Yl@o@nAS\\\\_@t@aA|B_@dA]|@YbAs@|Bs@rC[zACHEXQ|@O`A[dBANe@lD[jCm@hEa@dDYxBYvB{@fGIp@YzBSvAIh@WpB}@zGy@jGi@|DQrA_@vCMx@WnB?BQvA[|B?B]~BWrBe@lDOfASxAg@zDo@pFa@|DUrBQbB]tDUhCMxAS~BYvDOpBKlBYzESnDQzEO|EKfDIhDCxBAz@Ap@C|BEpE?pC?bB?xA@fB@pHBvB@~A?lBBfC@hB?|A@\\\\?h@B|IBdG?\\\\\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.7624508,\n" +
                    "                \"lng\": 108.5275979\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"102 km\",\n" +
                    "                \"value\": 101832\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 hour 13 mins\",\n" +
                    "                \"value\": 4380\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.471550199999999,\n" +
                    "                \"lng\": 107.5908997\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Continue onto <b>Jl. Tol Cikopo - Palimanan</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"tryg@qvxtSDfG?bCF`Q?n@?nBBdNBfFDfFJrIDt@@t@Af@Eh@Kj@G^ERC^?h@C~DApE?@?t@?RAtB?lAGbHIzHA`@?B?V?@ErEA`FA|CAt@?@?DAf@EtG?j@?BChCApA?T?@?F?dA?BAN@~IB`BBpCBjA@bA@p@@L?H@Z@`@JdF@|@BfAJ|DDlBBbA@~@?@@F?@?V?@@F?@DlB?Z?@DvBF~B@\\\\@DDz@@H?HBZFl@Ht@@BHf@PnAPv@RdATv@Tr@JX?@L^?@BBL^`@bA`ApBh@|@h@|@p@|@t@~@b@d@VV?@DDLLHHRR~@|@bBrAtAfATPlA`ApB`B@@^X@?XVTPr@h@~AvAnAtApAxAfA|Ax@tALP@B@@@BNZ@Bv@zAb@~@Xp@@FBF\\\\`AL^Rl@DLXhAH\\\\@DDPBHF^@F?@H`@TlAHt@Fb@P~A@J?D?@BX?F?@BXFhA@d@B`CGpEOjCKx@CVIl@AFA@In@W|AS`Aa@~AABK^ABM`@M`@AFGNCFi@|As@zAk@hAKRCDS^OVUb@A?GLy@dAg@p@s@|@yA|Ae@f@_@ZaAz@OJgAx@wBxAmDvB}@j@s@d@cBfAMJoA|@aAr@ABYXa@`@g@j@YZ]`@KJ{@lAeA~AeAtBYl@MVCDYx@O\\\\CH[`AYdAcAlEI`@IXAFGXCFGZiBzHADGZSz@ERmAzFa@lBAFI`@EZAF_@xBc@fDU|BIfA?FCRK~BI~BCbA?lA@rC?jAJbDLfCFr@PzBPzANjAFd@Lx@DZH`@TfAJd@H^f@|BhAvE`A|Db@fBZxARt@J`@@B?@HX@Dh@fCx@dDl@|B\\\\vAd@tB@HT|@J`@BL@@Rz@jAxEl@zDf@vBfAzEnAxF`BhJVhBt@jFZjBNxAR|BFz@Df@@LFfADf@Br@@d@DbABb@@X@Z?P@PF|BFtCFfFHrHB|ABtB@v@B|A?N?pBBjDBtC@rADtBB|@FpBLhCBh@Dp@J`Bf@rFPfBN~Ab@hDFb@TzARrAVzAHd@F`@Pz@F^FXZxAXxA`@bBLd@Np@^vATv@XdAXdAl@bBh@hBBJ`@hADL`@fAx@xBr@nBXr@p@~ApChHzB`GnBdFv@zBd@dA~@~Bt@jBx@xBjBxEf@xAd@jAt@jBt@pBjC|GdAlCxB~FfAvCdBpEr@fBhArCxBbG|A~Dv@pBrAjD^bAxAzDnA`Dj@zAjIjTlCzG`A`CfAzCr@pBN^x@zBd@xAh@hBNl@TdAJ`@J`@FZdAjEZnAXtA^nBl@jD^~B^bCXnBNzAV`CDXD`@BTFp@BLJdBFt@PdBDb@PjDJdCBbA@d@D~ADvD?pA@jBAnB?fBAlBApAI|CCv@GlAEz@Cb@Cb@Ex@OpBMhBO`B_@xDO~AMfAMv@Id@_@fCOhAKt@Q`ASbA[dBYrAMh@WfAi@zBQr@mAtEk@lBk@hB[bAe@rAa@nAe@rAe@pAaB~DaAxBc@`Ai@nAoA`CiBfDcAfBk@dAeAnBi@~@_A`B[h@gAlByCpFiB`DeAnBmAvBqAnCsAzCmB|E_@z@O^m@bBOb@q@pBc@tA_@nAk@lBe@dB[pAk@zB{@xDo@dD[tBc@hCu@xFa@tD[tDS`CKhBK~AIhBGtBGpBEbCAxACzB@|B@tBBhC@bAJjDD~ABpBNhHLlHFbCD~A@V@r@FzBBbBBbABjA@r@@Z?Z@b@H`CD~BB`A?RDtBDvBDlBDtBBbBDxBJ|D@r@HxDDdCBdABdBFnBB`BFrBBlABpABfADzBDfABvB@dB?fAAlA@X?N?HA~A?nAAt@ApBA~@IzCEpACb@KvCGhAEp@Cr@GfAOpCGh@c@pFO`BAHCPEf@Gh@Ed@e@jEY|BQvA_@|CM|@UbBIl@CJK~@]hCEVgAfIm@bFIp@O|AE^Ip@S|BUdDW|EALGnA?FKxBKtEElBAdBCfG@vB@t@?D?VBpDJdIBrCBfCJxIDpEBfBFtE@nAAjC@lBErFEnB?PE`BK`EAX?HIjBIdAEfAKlBC\\\\I|@C`@MvAWnCQ|A]xC[hCObAe@rCOpA{@pE]zAc@nBAF_BzG}@lDgB~FIZAD}@hC{A`EsAvDmEzJcDnG{BfEMR[f@oApBiAdB}AjCuBvCqDzEaCxCk@r@u@x@CDWX[^sAxAc@b@YX_C~B}GlGwDlDA@_DpCOLeB`BsAlAgB`BiAbAsBpB}CrC}AtAqE~DyAxA_Az@iC~B_A|@SPED_Ax@uBlBiD~CgC|BuBlBcB|AyCnCuAnAuBnB_DrCkAfAgC|BcA~@mPhOKHIJkHvGsClC{AxAORaA`AkApA{@~@_BfB}AlB}AlBgCfD_ApAu@dAi@x@SXiBnCu@lAoAvBgB|CoCdFeChF}AbDyAbDsBjEuBpEuBnEiBzDuArCaAnBo@pAgAxByAnCq@nAoAzBmAxBoAzBsA|BeC`EuAxB}@xAUZYd@wCnE{BfDs@~@kBfCqBrCy@fA{AnB_@f@{D|EmB~Bu@`AWVoC~CaDpDc@d@}GfHoArAiAfAKLMJgIbIwHtH[XYXYViCdCeG`G{CzCiCfCYXONkBdBiB~AaCnB_Ar@qB~AcDrC[Vu@n@A@sD|CuAlAcA`AIHs@p@k@h@GF{DxDgCnCoGnHsBlCeFpHiAdBy@nAS\\\\ED{DzGEHS^g@~@_AdBiCjFGNINyC`H]|@O^kB`FoAlD}B~H_ArDuG~WaBdHKf@}B~Js@`De@rBKh@K`@Md@cAfEWdA_@|Ak@dCm@dCOp@U`A]tA_@vAi@nBWdASt@aA~DOd@M`@u@jCqBvG?BM\\\\]`AgAfD{@|Bi@rAeAhCcAhCgBbEwAvCu@`B_@v@S^u@vAm@jAwAnCU^g@z@Wb@OV_@r@cA`B_CvDQVSZKRk@~@m@`ASZgAfBk@z@eA`BcA~AoArBS\\\\A@SZ{A`C{AbCsAvBo@dAsBdDuAxBYd@MTiApBSXwCxEU^OVm@z@iCbEyBdDmB~Cq@bAaAzAc@t@c@r@eBpCcA~AmAnBgBrC}@xA_AtAoApBs@jASZA@oAtBcAzA}C`Fo@fAa@l@k@z@CDU^SZS\\\\qApBiAfBm@`Am@`Aw@nA{@tAKNi@z@?@}@vAU\\\\aA~AmAtBmAtBgCvEe@bA_@t@_@r@EHQ^Q\\\\?@_@t@m@pAgA`CaAzBw@rBABO^O`@IPUl@O^IRUl@O^CJIR?@O^M^EJ_@dA[|@aBtF_@lAQr@a@vA]rAs@fC_@xAWhAKb@EVMf@_@`BYpAQr@c@rB]dBUfA]bBi@rCKp@Or@G^Q`AQfAMr@Mv@kCdNKh@q@jD_A`FUnAsAlH_DnPET{BrLuBzKI`@}@~EyC~OeExTcAnFg@jCc@~BsDtRmBdKIb@y@nESdAe@bCyE|VkBrJoBfKq@`Dg@fCaCtJqAvEcC|HeCdHCHuCzHeC~F_BnDoBpDINy@jB{@hBq@pAw@nAoBjD_AxA{@vAu@fAgAbBm@~@mAfBoAjBoAbBcBxBGHiB`C_D`EqCxD_BxByApBwAhBwClEiCbESX_AzASZk@|@qAfCgB~CsCpFo@pAm@jAaCdFYl@k@nAkAlC_A|BgAvCwAvDoAfDeA|CqAfEaA|C_AzCq@`C[jA[pAm@dCs@~Cy@tDm@~Cq@rDm@hDaAbGc@dDiAdJ{@dJ]dEGl@k@pI_@hIYlI?^MtHE~GA|BGrIGnI?JGpIAtII|HCzBEtFCvGM|G[tG]rFcAhIg@fDa@dCyA|GcA~D{@~CeA~Du@dDiAvEcAbEENwAxGaAzE{@rEqAtHcAhGy@vFs@dFy@|Ge@pEc@zD_AhKg@xGUjDI~@a@tH_@hI[pIQ`IIfHARIhIGdFChCE`GKpKKrJCvBIpDCz@GbBSzFQnDCXy@vM_@lFKxASxCIxAOtBKrAANa@pGEb@UrDC`@IfAmClX_@tCU`BUxAGd@Kp@Kr@GZQfAY`BUpAOz@WrAc@xB_AlEa@pBkCrKqBhHaBtFKXgBlFuEhMgArCyD~IqCdGYj@]r@EFc@v@w@vAi@`Ay@~Ak@`Ac@r@q@nAgBvCkAnBMVCDS^Wb@c@v@Q\\\\a@n@[f@?@SZMTm@fA[h@}@|Ac@p@m@fAi@dAo@lAe@|@k@fAi@jAm@pAo@~Ai@nAa@|@m@vA_@`Am@`BUj@ELGRO`@Ul@EPcAzCi@~AMb@M`@CJi@hB]rAa@xA_@vA]zAOr@Qx@c@nB[zAI`@AFg@vC_@~BQfAStAc@`DK|@G`@AJCVYpCKjAOfBUfCEd@YpDAXIxAQhDGfA?ZAF?`@OlHAzCF|I@p@@fCNnE`@`LRjCLvB^xFNvCPbDp@~Lf@pI@NNtCP|C^dFVdG?F?@?B@R?J@B?RDpAHlDBfD?L@bA@j@CjEKpEEhDCVc@lI[jD?BCRMrAK`AY~Ca@|C]|BeAbGa@tBKf@Op@ELS~@CHETMd@e@vBk@nCu@fDe@dBCFo@`DADEJsAlFS|@Kb@e@hBUbAm@`CGXWpAS~@?BO`AKf@]fBU~@Qr@kAhEGTi@dC_@bBGZUjAu@zCWbAe@lBw@jDy@jDkA`Fw@jDy@jDiA`Fq@xC[rAEPABYjAK`@m@fC}AzG_@|AKb@GTYnA_BhH[tA_@`BaDlNkDjOm@bCYjAy@jDI`@GTgChL_BbK{@|HIx@MtA]tDIbACb@g@dGIz@Eb@k@vGi@~FOjBa@~CCNe@~COp@}@nEcArDe@xAoAzDEHmBzECDiBrDqA`CWd@OT{AbCuC|Dk@p@UZg@l@a@`@iCnCyAtAu@n@i@f@yGbFuEzC[T_IhFiCfBuAbAC@mBtAw@l@_BnAOJiBbBuAjAaBfB_CdCuB~Bi@r@cAtAIJyB~CSZoAhBYh@S\\\\g@~@kAtBc@z@w@~AOZgAbCw@nBcArCQh@kApD{@pCu@zCo@xCQt@y@xEo@xEa@pDCV[`ESjD?BIrDChAGpC?nC?b@?jB?dB?DBpCBbLFrIBdEBrH?jB?b@@fA@xE@fA@tD@rD?pAFxLDbHBhF@VFzDNxD\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.6898705,\n" +
                    "                \"lng\": 108.4300083\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"14.9 km\",\n" +
                    "                \"value\": 14923\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"11 mins\",\n" +
                    "                \"value\": 647\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.4382457,\n" +
                    "                \"lng\": 107.4662742\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Keep <b>right</b> to stay on <b>Jl. Tol Cikopo - Palimanan</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"maneuver\": \"keep-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"d~nf@cztoSFlA`@|G^|D?@Db@ZpCNfAVhBl@tDf@lCbArEt@tC~AfGFRLd@p@fCNh@V`AjBfHfAhELd@VfAl@jCVpAd@jCPbADZ@FJz@X`CJ~@JfAFz@LtBJ`CFvC@nABdA?|@CvBEtCEbBG~@KxAE~@Gn@MvASfBWpBAFUxAETA@UhAKj@Qz@Kh@U`AYjAe@hBq@pCk@~Bm@fCUbAg@rBABq@nCOp@Qr@WlAQv@[`BQ~@SxAO~@K`ASjBIfACd@C`@IdAIvAAl@An@?PApA@|@@f@A`@GpB?pA?pA?pAFbD?hEDlBBtFDpF?fAAvAAJAh@CfAGnAE|@GlAGhAI`AE\\\\ALKv@Ip@Kp@M|@Q|@e@xB[zAUx@Y`Ag@dBYz@_@`AYv@O`@s@rAiAdC[n@c@r@a@l@s@hAa@j@k@v@s@`Ao@t@y@z@s@x@YXWRcA`Ag@f@y@|@eAhAoAnA]^KHm@n@QPcAdAs@x@i@p@g@p@i@v@w@jAc@p@_@l@GLYh@OVa@`AiAzBWh@Yn@y@dCi@`BSx@U|@U~@UdASdAGZUlAADSjAO~@SfAK^I`@]~AUfAMx@QzAObAQjAOrA_@bC[tBYlB_@dC[pBOjAOlASrA_@lBQbAWvAMl@K^S|@G`@Mz@SnB[bCa@zCe@hDGb@Kz@]xBe@jCSfA[xAY|AW|AU|AQfAWfAU`Aa@xA[hAYhAEN[dAs@fBa@bAe@`AYl@s@zAg@dAm@fAKN[h@QXQV[^UZw@~@aApAg@j@eAnAuAzAeAfA[XYXCBURu@t@y@z@aA`A_A~@aA`A]`@ONq@r@MLKJc@d@m@n@i@j@e@l@iAvAwAfBwAlBw@fAa@l@GJeAbBq@dAm@jAg@`Ak@lAq@xASb@i@lAo@`Bg@tA]bAYz@Sl@m@lB]dAy@vCk@vBOl@Op@e@dC{@lEk@nCETI`@I^[xAUdAMr@CJSdAQv@GXEXWpAQt@Kf@I^Oh@UfAUjAUdASdAMl@I^?BkAdFAH_AfEWjAQ~@WjAWlA{@hEQ|@Q~@CN}ArHYtAS`AOp@i@jCaAbF\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.471550199999999,\n" +
                    "                \"lng\": 107.5908997\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"24.1 km\",\n" +
                    "                \"value\": 24136\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"21 mins\",\n" +
                    "                \"value\": 1233\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.3494354,\n" +
                    "                \"lng\": 107.2772785\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Continue onto <b>Jl. Tol Jakarta - Cikampek</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"`nhf@eo|nS}@nDqGf\\\\{BdLaDfOoCjLu@xC_CnIe@bBsFlR?@A@Md@w@lCe@fB{AxECHaAhD{@pCg@bBWz@wKt^Sp@_AhDaDjKQn@cClImAhEM`@g@fBc@tAEJY`AwApEUx@g@bB?BEL_CbIYbAY`AUn@y@`CsA|DITm@rAO^Q^ADq@xAO^GJ]p@QZ?@}@~AyAdCeAbBA@CDW\\\\eB~BmAzAy@~@SPk@p@_A`Aq@r@cA~@}@x@CBUR[Vu@l@{BfBk@`@aAp@sA|@mC`B_@PgAl@}Ax@{CvA_@NsAh@sAf@u@Xa@Nq@Tm@RoA\\\\cBd@gAVm@NC@A?GBo@NgB`@_B\\\\IBeEx@eCd@a@JyE~@gCj@C?oBf@sCt@qA^aCv@k@TuAf@gBr@qBz@}Av@]N_@PgAn@]PaB`AuAz@kBrAEDyAbAKHy@p@sAfAmAbAkBhBaBbBiBrBuAbBiBbCeAzAQXq@hAkAvBkArBOZaArBkA~BCHi@hAA@a@~@oAhCWh@k@bA[l@Q\\\\GNi@rAmAdC_AnBINkCzFkB~DeChFEFMX?@CDkBbEyB|EeAxBCDgE|Ig@dAs@xAKVCFWd@k@nAiAzBiAdCmF`LiFbLw@~Ac@~@mCxFWh@Q\\\\kB~Ds@zAaAnBcDbHmBbEoAlCoBfEg@bAg@dAkAbCgBtDq@rAgCvFe@~@Yl@Wh@c@`Am@pA{@lBu@bBUd@Ub@c@|@KVU`@}@nBmAdCYn@mAdCw@~AyA|CoAvC[n@m@pAcAxBkA`C{@hBc@|@i@hAoAjCeBpDEFe@dAg@dAiB|D{DlImDrHQ^a@|@Q^{AxCQ\\\\Ud@]t@gAdCaAvBuDfJGLa@bAKRmA|CKXgCfHg@jAw@xBKZKVGRABIRi@bBYv@Qh@g@dB[`AADmA`E]`A[jA[jAsAxEgBzGkAxEi@hCADERYpAwBfKSz@wBpLKl@]rBs@pEGZIZQ~@QxAc@tDsBbQ_AzIEp@k@`IIfAQbCKpAQnCMbBIdBMlBMpCM`DOtDE~AKvDC`AOpHGbCA|@e@hUARCvAATAp@AHIvCMrIIdEYvLGvEMrFAn@AN?BEpCKfFAJ?@?NK~FK`EEbDClAANAb@?\\\\?DAb@ItEGrC?@G`CGlDCnBANA|@Al@GjCIzCQpEAVAVGvFK|E?FAZ?FAZGrBAb@EpA?b@\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.4382457,\n" +
                    "                \"lng\": 107.4662742\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"23.2 km\",\n" +
                    "                \"value\": 23158\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"17 mins\",\n" +
                    "                \"value\": 1026\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.2867071,\n" +
                    "                \"lng\": 107.0856364\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Take the exit toward <b>Jl. Layang Sheikh Mohammed Bin Zayed</b>\",\n" +
                    "              \"maneuver\": \"ramp-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"~bwe@_rwmSBLFn@CbB?p@AbBAf@?v@EvACf@GjCIvBAJAn@G|BADCdAAb@?TEbBAb@AL?LCtCAb@?BA`@Ab@?@Ad@?b@CfA?JAVAZ?b@?@Ad@Ab@CjBGjD?HCb@Ab@EfAAb@Cb@Ab@Ah@C`AAb@Ad@AfACfACfAGlCCfC@n@BnC@fA@f@DlC@b@?TJbCBXJdBRnC@BDb@RhBV|BZzBPrA`@|Bf@hCH`@rAnFDP^rAj@rB`@tAPj@v@xBz@bCTr@Tl@h@bBN^h@bBL\\\\Nb@`B~E@F~AjFJ\\\\XdAXbANl@^xAH`@RdARdALv@TrAHn@l@vEHv@`@tDX|D@RXvH?L@fA@fA@b@?\\\\?h@Ab@AtCIjCAb@Ad@?@A`@Ab@Cn@Ad@?TGn@IfACb@MjBCXStBC^g@rD?@MbAMfAEb@CRu@dGEb@Gb@MdAk@xDo@nFG`@ALq@pFa@`D_@tCG`@Gb@OdA?@MdA]jCKr@W|BUjBe@nDYrB[bCE^c@lCEVCJOlAS`BMfAEb@OlAe@zDIt@k@rE_@tCm@hEIb@oAdG[pAg@nBs@bCw@|BO`@AD_A`CKXQ^O^O^Q^e@fAeB|Da@t@g@z@y@zAYd@w@nAsAtBiAdBy@dA{AlBmAvAi@n@q@v@gAlAmBtBw@~@aE|EY^uAjBk@x@yChE{AfC}CvF}@hBu@bBQ`@?B_BdEaBxEkA`EkAdE]nAIXIZM`@kDxNkA~DcBhHI\\\\K^S|@eCfJkAbFoChK{B~IeA`EgArEAB{@hDiAnE}AjFGVIXcAjDgA~CoAvDUt@IRwBtGGNM\\\\M\\\\uBvFwApDwChHQb@uDbI[p@IPIPiEfJaEhIwDvHaIrPi@fAwD|HiCjFEHINIPeBpDiChFoB`EoDtH}@dBsAnCQ\\\\GN]p@kBzDuD|HwBhEEFKVMT_CrEkClF_ClFoBzDcBpDuDvHcC~E{CjGcBjD]v@}@lBwAvCs@|AQ\\\\Q\\\\q@xAk@bAeBhD_DhGiCjFmAhCaBpDYj@oGrMe@`A}C|GwFpLKRIRGLcFfK{@fBQ\\\\u@|AuAtC{FnLqDhICDMXO^sA~CcC`G}BbGm@`BM`@O^i@zAkAjDO^Wr@iBrFA@Md@Od@mBpGc@~Ag@dBEJSt@oBrHc@bBq@pCERK\\\\I\\\\cAlEkApFAFSz@Ib@a@dBUhAENCN\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.3494354,\n" +
                    "                \"lng\": 107.2772785\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.2 km\",\n" +
                    "                \"value\": 152\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 7\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.2863178,\n" +
                    "                \"lng\": 107.0843225\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Continue onto <b>Jl. Tol Jkt - Cikampek Layang</b>\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"|zje@gdrlSADI\\\\aAbF\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.2867071,\n" +
                    "                \"lng\": 107.0856364\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"14.5 km\",\n" +
                    "                \"value\": 14531\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"11 mins\",\n" +
                    "                \"value\": 666\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.255837,\n" +
                    "                \"lng\": 106.9612208\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Continue onto <b>Jl. Layang Sheikh Mohammed Bin Zayed</b>\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"nxje@_|qlSER[zAy@pDGZIXS`A_DhOUdASbAIb@_@bBeAnEKb@K`@UbACH]|AI`@aAjE_@hBa@fBUjA_@bBI^I^K`@I`@Ib@GTc@rBUbAUdAI^UfAUbAI`@Ib@K`@I`@GTMn@I`@K`@I`@Kb@c@rBQv@S`ACNEP}@hEIb@e@bBe@dBK`@YbAK`@g@bBYbAK`@YbAIXAFK`@g@hC{@fEYjAU|@AFI`@i@hCUdA_@hBI^K`@y@jDK`@K`@GTETK`@K`@K`@c@dBMd@St@K`@K`@K^M`@K`@WbAOf@a@~AYdAI^UdAUbAEPENUdAK`@ADGZi@~BCHELCJK\\\\a@lAkBnFe@xA]`AM`@M^gAdDIZMXKTABMZO^oBxEwArEQf@aAzCu@|BMf@yB`IsBnIkBdHiAjEK^}BnJkBzHoC|K{@fDGTGTg@lBqC`LaAtDiDjNADIZIZeAjEyB|Iu@tCk@~Bu@pCI^K^ADeBdHAFaCjJgBjHg@nBGXIZsArFqC|KGVkCrKcDtMEPOj@iCfL_CbJ_ChJ_ArDGVIXq@lCsBlI{BrIq@hCmAjEQn@GN}BlHcCzG_BvD[t@A@{E`Lw@rBuArE{@tCi@tC]rBABGh@Il@CXIlAGbAARCj@A`AAbA?FAZA\\\\A^Aj@AhA@h@BdADtAFxALpANnBXpBXbB`@hB\\\\tAXjAX|@`@nA`@~@|@pB~@jBnDlGz@|ANXNX~@`BhDlGtA`DxAtD~BtGJXJZRf@Pf@zBxI\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.2863178,\n" +
                    "                \"lng\": 107.0843225\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.5 km\",\n" +
                    "                \"value\": 525\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 27\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.256582400000001,\n" +
                    "                \"lng\": 106.9565648\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Take the exit on the <b>right</b> toward <b>Lkr. Dalam</b>/<wbr/><b>Pd. Gede</b>/<wbr/><b>Cawang</b>\",\n" +
                    "              \"maneuver\": \"ramp-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"~yde@szykShAnFPhBXlC@TBZDr@Dp@Dn@FhADrA@h@Ij@\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.255837,\n" +
                    "                \"lng\": 106.9612208\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"9.1 km\",\n" +
                    "                \"value\": 9117\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"8 mins\",\n" +
                    "                \"value\": 497\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.247636200000001,\n" +
                    "                \"lng\": 106.8805438\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Merge onto <b>Jl. Tol Jakarta - Cikampek</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"maneuver\": \"merge\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"r~de@o}xkS@PFfC@f@FzA@\\\\?D@h@DlBHrDHbDHvD?hB?D?NB`AJjFFhBTtJ@l@BpADbB@|@HbEBfC?\\\\@tAFzA?FBh@NrIBn@?z@?PL~F`@bRTpML|H@j@B~@@`A@b@DfCF|BRvKVxNHpBHnEDtB?HJzD?@HrG@fAFtD@b@?LBpD?tFKfEKbBEp@I`ASjBi@dEaCxKmB`GKZYbAuAtEK^Sp@u@rCa@zASl@Wz@M`@g@tAK`@_@~@i@nAcAzBCFQZi@x@GLyAvB_AlAqAvAsAlAcCfB}CfBaEnBwBz@gCnAy@d@oAt@a@T_BdAiBpAuBhBeAfAuAlBcA|AgAxB_@dAUj@[hA[|ASjAIl@G`AIrAA~@B~@Dr@LzAPzAHd@HXT|@FRVf@v@xAzBrDx@rAz@rANXx@lAl@nANZpA`DPd@L^J`@Jb@H^F^F`@DZ@PBPB^@`@@l@TnG?@?t@@tBA`AClAEtA?D\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.256582400000001,\n" +
                    "                \"lng\": 106.9565648\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"1.2 km\",\n" +
                    "                \"value\": 1188\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 82\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.241290999999999,\n" +
                    "                \"lng\": 106.8774542\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Take the exit toward <b>Jatinegara</b>/<wbr/><b>Kelapa Gading</b>/<wbr/><b>Tj. Priok</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"maneuver\": \"ramp-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"vfce@kbjkS@H?F?PALCj@E`AIt@APQvAE^ABAP?PALAT?B@V?F@TD`@BRH`@BR@HBPD`@?\\\\?X?BAPAD?FCNEPABKXA@QXCBGFIH]RGBa@LA@UDm@DA?S?SAo@GGAYMQGe@Se@UeAa@a@Q]Kc@Ma@IQCm@G_BMy@IMAi@EWAo@GOAQAo@GWEa@Iy@Qy@QGEKG\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.247636200000001,\n" +
                    "                \"lng\": 106.8805438\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"4.2 km\",\n" +
                    "                \"value\": 4168\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"4 mins\",\n" +
                    "                \"value\": 238\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.2043075,\n" +
                    "                \"lng\": 106.8734486\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Merge onto <b>Jakarta Inner Ring Road</b>/<wbr/><b>Jl. Pulomas - Cawang</b>/<wbr/><b>Jl. Tol Cililitan 2</b>/<wbr/><b>Jl. Tol Ir. Wiyoto Wiyono</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"maneuver\": \"merge\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"`_be@aoikSu@Og@IQGKCWIYGOEE?e@KMAGAOCC?KAEAQAQCE?c@E[ASCC?KA}@?o@Bk@Bs@H{AReAPc@FsEv@w@J}Ez@YFoFr@aANaAPyATsEr@s@LYDWDaDj@sBZyDp@C?K@MBSBKBK@oEf@YByBVyFn@Y@WBi@DeBJuCPeBL]@_CHQ@A?O?_@BiAB[@aCDA?O@O?oCDc@@u@@yABc@@c@?gAB}CDiGJgBBI?Y@A?[@G?q@BuBDiDBgA@G?[@{FFc@@mCBa@@_@?C@[?cCDkBDc@@G?cBBU@U?cBDgBBsDHi@Bk@Bs@H}@HkBTQD\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.241290999999999,\n" +
                    "                \"lng\": 106.8774542\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.5 km\",\n" +
                    "                \"value\": 501\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 37\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.1998625,\n" +
                    "                \"lng\": 106.8733227\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Take the exit toward <b>Rawamangun</b>/<wbr/><b>Salemba</b>/<wbr/><b>Pulo Gadung</b><div style=\\\"font-size:0.9em\\\">Toll road</div>\",\n" +
                    "              \"maneuver\": \"ramp-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"|wzd@avhkSy@Ta@Fc@FUD{@JE@]@E?m@DY@A?c@@]@a@BU@w@Ai@C}AEA?k@CYCmAIa@GYEg@G\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.2043075,\n" +
                    "                \"lng\": 106.8734486\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"32 m\",\n" +
                    "                \"value\": 32\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 4\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.199593,\n" +
                    "                \"lng\": 106.8734196\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Merge onto <b>Jl. Jend. Ahmad Yani</b>\",\n" +
                    "              \"maneuver\": \"merge\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"b|yd@guhkSECKCc@K\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.1998625,\n" +
                    "                \"lng\": 106.8733227\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.5 km\",\n" +
                    "                \"value\": 463\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 59\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.1955174,\n" +
                    "                \"lng\": 106.8742802\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Continue straight to stay on <b>Jl. Jend. Ahmad Yani</b>\",\n" +
                    "              \"maneuver\": \"straight\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"lzyd@{uhkSCAiBYgC]OCOCA?a@IuAOSCOC_@EI?g@CcAMc@GoAOMC_@Eg@GYE\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.199593,\n" +
                    "                \"lng\": 106.8734196\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"46 m\",\n" +
                    "                \"value\": 46\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 6\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.1951154,\n" +
                    "                \"lng\": 106.8743436\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Keep <b>left</b> to stay on <b>Jl. Jend. Ahmad Yani</b>\",\n" +
                    "              \"maneuver\": \"keep-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"~`yd@g{hkSI@C?IA]EYE\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.1955174,\n" +
                    "                \"lng\": 106.8742802\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.4 km\",\n" +
                    "                \"value\": 361\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 42\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.1922356,\n" +
                    "                \"lng\": 106.8733741\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Slight <b>left</b> onto <b>Pramuka Barat</b> (signs for <b>Pramuka</b>/<wbr/><b>Salemba</b>/<wbr/><b>Senen</b>)\",\n" +
                    "              \"maneuver\": \"turn-slight-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"n~xd@s{hkSQ@K@G?I?EAMA_@IaBQK?M?I@I@IBEBGBMJa@f@QPOJ[T]P_@PGBc@P[HODQF]R\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.1951154,\n" +
                    "                \"lng\": 106.8743436\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.6 km\",\n" +
                    "                \"value\": 556\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 53\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.1924093,\n" +
                    "                \"lng\": 106.868363\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Turn <b>left</b> onto <b>Jl. Pramuka</b><div style=\\\"font-size:0.9em\\\">Pass by LIA (on the left in 500&nbsp;m)</div>\",\n" +
                    "              \"maneuver\": \"turn-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"nlxd@quhkSCTCVC^Cv@Ch@AH?VAl@?N?\\\\@n@?t@@VBjA@HDhABb@@P?H@NBf@HnAFxAHhB@B?L\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.1922356,\n" +
                    "                \"lng\": 106.8733741\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"1.5 km\",\n" +
                    "                \"value\": 1522\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"2 mins\",\n" +
                    "                \"value\": 143\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.1977703,\n" +
                    "                \"lng\": 106.8561172\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Keep <b>right</b> to stay on <b>Jl. Pramuka</b><div style=\\\"font-size:0.9em\\\">Pass by FAUZI MEDICAL (on the left in 1.5&nbsp;km)</div>\",\n" +
                    "              \"maneuver\": \"keep-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"pmxd@gvgkSM?@`@@RLzBFtABf@FtAHrAJnA@RFp@@FBPBRLdAHf@BPJj@Ln@DVFVRt@Tn@Lb@Pf@HRJVLZDJZp@HRP\\\\P\\\\^n@Vd@Xd@R^\\\\n@R\\\\f@x@b@r@Xh@Xj@JRx@tANVf@z@LPDHBFDFBD?@DF@@DH`@n@f@x@R^LRVf@DFFHFHDBJFj@`ADFDFb@x@HPLTDF\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.1924093,\n" +
                    "                \"lng\": 106.868363\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"11 m\",\n" +
                    "                \"value\": 11\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 1\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.197823899999999,\n" +
                    "                \"lng\": 106.8560338\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Keep <b>left</b> to continue on <b>Jl. Pramuka</b>/<wbr/><b>Jl. Pramuka Sari II</b>\",\n" +
                    "              \"maneuver\": \"keep-left\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"`oyd@wiekSHP\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.1977703,\n" +
                    "                \"lng\": 106.8561172\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.9 km\",\n" +
                    "                \"value\": 906\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 88\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.2027395,\n" +
                    "                \"lng\": 106.8495376\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Keep <b>right</b> to continue on <b>Flyover Pramuka</b>/<wbr/><b>Jl. Layang Pramuka - Matraman</b>/<wbr/><b>Jl. Pramuka Sari II</b><div style=\\\"font-size:0.9em\\\">Pass by Menteng Square (on the right in 550&nbsp;m)</div>\",\n" +
                    "              \"maneuver\": \"keep-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"joyd@eiekSLR`@p@^j@HL^l@JPFJTZ`AnAh@p@HJFJ^b@\\\\b@`@f@^d@h@p@Z`@HJBBLN`ApA\\\\b@HJl@t@LPLNPTDBT^HPb@~@FPLXFNBFTb@LRTd@r@jAHJHLLNPF\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.197823899999999,\n" +
                    "                \"lng\": 106.8560338\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.6 km\",\n" +
                    "                \"value\": 581\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 77\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.2078937,\n" +
                    "                \"lng\": 106.8489265\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Continue straight onto <b>Jl. Tambak</b> (signs for <b>Manggarai</b>/<wbr/><b>Bukitduri</b>/<wbr/><b>M</b>)\",\n" +
                    "              \"maneuver\": \"straight\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"bnzd@s`dkSD@NBH?H@tCVJ@LBB@F@v@H@?D@F?L@jHr@\\\\DH@V@R@L@\\\\BzALR@R?ZAVAdBO\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.2027395,\n" +
                    "                \"lng\": 106.8495376\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.2 km\",\n" +
                    "                \"value\": 212\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 34\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.2092334,\n" +
                    "                \"lng\": 106.8476558\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Turn <b>right</b> onto <b>Jl. Sultan Agung</b>\",\n" +
                    "              \"maneuver\": \"turn-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"hn{d@y|ckSLBHDJFJHLNTZFH@B@BFFLRXf@FNNV@BBDDDD@JHB?THZJND\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.2078937,\n" +
                    "                \"lng\": 106.8489265\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"distance\": {\n" +
                    "                \"text\": \"0.2 km\",\n" +
                    "                \"value\": 234\n" +
                    "              },\n" +
                    "              \"duration\": {\n" +
                    "                \"text\": \"1 min\",\n" +
                    "                \"value\": 50\n" +
                    "              },\n" +
                    "              \"end_location\": {\n" +
                    "                \"lat\": -6.2087663,\n" +
                    "                \"lng\": 106.8455976\n" +
                    "              },\n" +
                    "              \"html_instructions\": \"Turn <b>right</b> to stay on <b>Jl. Sultan Agung</b>\",\n" +
                    "              \"maneuver\": \"turn-right\",\n" +
                    "              \"polyline\": {\n" +
                    "                \"points\": \"tv{d@{tckSANMp@AHG\\\\CH?DADETGd@]lCG~@EJAB?@\"\n" +
                    "              },\n" +
                    "              \"start_location\": {\n" +
                    "                \"lat\": -6.2092334,\n" +
                    "                \"lng\": 106.8476558\n" +
                    "              },\n" +
                    "              \"travel_mode\": \"DRIVING\"\n" +
                    "            }\n" +
                    "          ],\n" +
                    "          \"traffic_speed_entry\": [],\n" +
                    "          \"via_waypoint\": []\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"overview_polyline\": {\n" +
                    "        \"points\": \"t{ah@{tpuSp@wBpADnAq@nAlBrDzC`CxB~QhNdM|ItF|K|KvHpm@dVf\\\\fMhDjFdMxPha@~c@lLzLxBm@YyDkKwMaCg@gNpJqa@xVgl@pR{yCf|@k]hNs`@rZoq@raAsc@rp@yO`WaGvOeL~u@uKhy@oKxdAsBxlAb@lmAId_Ac@b|AzAjp@lDjPzIlNjHlGhHzFfMzN|FnO|AfLUzTgBjJkG`O_IdJuSlN_HdG}G|KyIr]iCjM}BbWp@lY|AlKzKfe@bRx|@fDz_@dAvz@fGrm@rJv^rhArvCz[j}@nJlk@vAnp@mEfj@cNvj@u_@dv@aQn^aHdUkI`i@mAzZvAb`AbAfh@vAlt@u@fc@gPl`Be@t{BuHtn@wHbZeWrk@cW~]gRdR{r@to@g`A|{@oU|T_S|V_V`c@kVbh@sg@`z@gm@`s@y|@dz@oQtOu_@|c@{K~QoOr]w`@h_B{Obl@sZvq@uVba@}_An{Aan@fdAeVvp@qf@`fCul@j_D_Kr_@eLjZiPt[oh@lt@oRh[eQ~^{Pxg@aJvb@}Id_A_Apv@kAbdAkDjZ_H|XqNpq@}Kv`A{Czx@aAj~@uG|hA}Kv{@kWd}@mPp^kQ`[{R``@aLv\\\\sJhh@yCtb@~@~u@zDdr@bBhe@wAhc@_F~]kKld@sJfb@if@`vBgPlv@qDl`@gEh`@oJd[wO`W}ZlWeY`SuSrTiKnPmKzWcIzb@eAt\\\\`@poAtA`u@~DjZ`Tl|@zAzOH~YqEr]kMbk@aBv]Grt@oHd_@}HnOqI~JuVfYeM|]oR`mAuK|m@yMlYeXtYqUfZwQ~f@gIpa@gj@bmCmbAnkDaPnd@qVb\\\\sKrIwTjLeY`Iw]tHkTlIiOnJuRlRoWle@sZfp@ymAviCqu@n_BeZhq@{Ode@uVbjAeLvnAkGrvCkEtaCk@xVTng@vClVjGvVtPrh@lDzSdBt[a@~UwPvwAsI~p@wHv\\\\aDbIuL~T_Q|SiXl`@_Nz_@q`@r}AeWby@yzAb`Dy`BdhDka@|~@aU`v@gZfuA_Qnx@aU`aAqRps@ePle@}g@xrBe}@dqDwRjn@kO`e@}@dTdBvTtHvTdTfb@tKna@lAj[`Bvy@`GncDa@xZuLbh@yEdPoHnNiJzI_RbJgShPwGjRT`OlRj`@zAvYWtZoCxD_EMiHiCyQuBgH_BoO^_w@bMeb@zDoXl@maAvAs\\\\dC}PIi`@_F_E]iBbAcEvBsApEVrOtAt[lDvOtJvQ~LpSlVn\\\\lGrKhSpBvFVpEt@fDnDkA`L\"\n" +
                    "      },\n" +
                    "      \"summary\": \"Jl. Tol Cikopo - Palimanan\",\n" +
                    "      \"warnings\": [],\n" +
                    "      \"waypoint_order\": []\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"status\": \"OK\"\n" +
                    "}";

            if(response!=null){
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray jsonArray = responseObject.getJSONArray("routes");
                    JSONObject resultObject = jsonArray.getJSONObject(0);

                    String polyllinePoint = resultObject.getJSONObject("overview_polyline").getString("points");
//                    List<LatLng> decodePath = new ArrayList<>();
//                    decodePath.addAll(PolyUtil.decode(polyllinePoint));

                    List<LatLng> decodePath = PolyUtil.decode(polyllinePoint);
                    Log.e("decode length ", decodePath.size()+"");

                    PolylineOptions lineOptions = new PolylineOptions();

                    mMap.addPolyline(lineOptions.addAll(decodePath)
                            .width(8f).color(Color.argb(255, 56, 167, 252)))
                            .setGeodesic(true);
                    mMap.addMarker(new MarkerOptions().position(pickUpLatLng).title("Lokasi Awal"));
                    mMap.addMarker(new MarkerOptions().position(locationLatLng).title("Lokasi Akhir"));

                    JSONArray arrayLegs = resultObject.getJSONArray("legs");
                    String distance = arrayLegs.getJSONObject(0).getJSONObject("distance").getString("text");
                    String duration = arrayLegs.getJSONObject(0).getJSONObject("duration").getString("text");

                    double price_per_meter = 250;
                    double priceTotal = Double.parseDouble(arrayLegs.getJSONObject(0).getJSONObject("duration")
                            .getString("value")) * price_per_meter;

                    tvDistance.setText(distance);
                    Log.e("data price ", priceTotal+"");

                    LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
                    latLongBuilder.include(pickUpLatLng);
                    latLongBuilder.include(locationLatLng);

                    LatLngBounds bounds = latLongBuilder.build();
                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int paddingMap = (int) (width * 0.2);

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
                    mMap.animateCamera(cu);

                    mMap.setPadding(10, 180, 10, 180);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


//            if(response!=null){
//                try{
//                    JSONObject responseObject = new JSONObject(response);
//                    JSONArray jsonArray = responseObject.getJSONArray("result");
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }

            Log.e("Response get ", "Response from url: " + response);
            return response;
        }
    }

}
