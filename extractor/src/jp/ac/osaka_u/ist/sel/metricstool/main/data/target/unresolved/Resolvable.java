package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


/**
 * 
 * @author y-higo
 *
 * @param <T>
 */
public interface Resolvable<T> {

    /**
     * ���O�������s���C�����ς݃I�u�W�F�N�g��Ԃ�
     * 
     * @param usingClass ���O�������s���G���e�B�e�B������N���X
     * @param usingMethod ���O�������s���G���e�B�e�B�����郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * 
     * @return �����ς݂̃I�u�W�F�N�g
     */
    public abstract T resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager);

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     */
    public abstract T getResolved();

    /**
     * ���ɖ��O�������ꂽ���ǂ�����Ԃ�
     * 
     * @return ���O��������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public abstract boolean alreadyResolved();
}
