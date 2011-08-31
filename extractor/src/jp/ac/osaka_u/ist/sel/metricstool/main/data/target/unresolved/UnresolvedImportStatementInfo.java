package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * static�C���|�[�g��\���N���X
 * 
 * @author higo
 *
 */
public abstract class UnresolvedImportStatementInfo<T extends ImportStatementInfo<?>> extends
        UnresolvedUnitInfo<T> {

    /**
     * �C���|�[�g������ƑS�Ă̖��O�����p�\���ǂ�����^���ăI�u�W�F�N�g��������
     * 
     * @param namespace ���p�\���O��Ԗ�
     * @param all �S�Ă̖��O�����p�\���ǂ���
     */
    public UnresolvedImportStatementInfo(final String[] namespace, final boolean all) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace || ((!all) && namespace.length == 0)) {
            throw new IllegalArgumentException();
        }

        this.importName = Arrays.<String> copyOf(namespace, namespace.length);
        this.all = all;
    }

    public abstract T resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager);

    /**
     * �ΏۃI�u�W�F�N�g�Ɠ��������ǂ�����Ԃ�
     * 
     * @param o �ΏۃI�u�W�F�N�g
     * @return �������ꍇ true�C�����łȂ��ꍇ false
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof UnresolvedImportStatementInfo<?>)) {
            return false;
        }

        final String[] importName = this.getImportName();
        final String[] correspondImportName = ((UnresolvedImportStatementInfo<?>) o)
                .getImportName();
        if (importName.length != correspondImportName.length) {
            return false;
        }

        for (int i = 0; i < importName.length; i++) {
            if (!importName[i].equals(correspondImportName[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * ���O��Ԗ���Ԃ�
     * 
     * @return ���O��Ԗ�
     */
    public final String[] getImportName() {
        return Arrays.<String> copyOf(this.importName, this.importName.length);
    }

    /**
     * ���S���薼��Ԃ��D
     * 
     * @return ���S���薼
     */
    public final String[] getFullQualifiedName() {

        final String[] importName = this.getImportName();
        if (this.isAll()) {
            return importName;
        }

        final String[] namespace = new String[importName.length];
        System.arraycopy(importName, 0, namespace, 0, importName.length);
        return namespace;
    }

    /**
     * ���̃I�u�W�F�N�g�̃n�b�V���R�[�h��Ԃ�
     * 
     * @return ���̃I�u�W�F�N�g�̃n�b�V���R�[�h
     */
    @Override
    public final int hashCode() {
        final String[] namespace = this.getFullQualifiedName();
        return Arrays.hashCode(namespace);
    }

    /**
     * �S�ẴN���X�����p�\���ǂ���
     * 
     * @return ���p�\�ł���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean isAll() {
        return this.all;
    }

    /**
     * ���O��Ԗ���\���ϐ�
     */
    private final String[] importName;

    /**
     * �S�ẴN���X�����p�\���ǂ�����\���ϐ�
     */
    private final boolean all;
}
