package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;


/**
 * �������ȁC�O���̃��j�b�g�������Ƃ�\���C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface UnresolvedHavingOuterUnit {

    UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit();

    void setOuterUnit(UnresolvedUnitInfo<? extends UnitInfo> outerUnit);

    UnresolvedClassInfo getOuterClass();

    UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit();
}
