package com.tangl.tanglapp;

public class HairIngredient {
    private final String mINCIname;
    private final String mCosingRefNum;
    private final String[] mChemIUPACNameDesc;
    private final String mCASnum;

    public HairIngredient(String INCIname, String mCosingRefNum, String[] chemIUPACNameDesc,String CASnum){
        this.mINCIname = INCIname;
        this.mCosingRefNum = mCosingRefNum;
        this.mChemIUPACNameDesc = chemIUPACNameDesc;
        this.mCASnum = CASnum;
    }
}
