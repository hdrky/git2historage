package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * AST�p�[�X�̍ہC�Q�ƌ^�ϐ��̗��p�\�Ȗ��O��Ԗ��C�܂��͊��S���薼��\���N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedClassImportStatementInfo extends
        UnresolvedImportStatementInfo<ClassImportStatementInfo> {

    public static List<UnresolvedClassImportStatementInfo> getClassImportStatements(
            final Collection<UnresolvedImportStatementInfo<?>> importStatements) {

        final List<UnresolvedClassImportStatementInfo> classImportStatements = new LinkedList<UnresolvedClassImportStatementInfo>();
        for (final UnresolvedImportStatementInfo<?> importStatement : importStatements) {
            if (importStatement instanceof UnresolvedClassImportStatementInfo) {
                classImportStatements.add((UnresolvedClassImportStatementInfo) importStatement);
            }
        }
        return Collections.unmodifiableList(classImportStatements);
    }

    /**
     * ���p�\���O��Ԗ��Ƃ���ȉ��̃N���X�S�ẴN���X�����p�\���ǂ�����\��boolean��^���ăI�u�W�F�N�g��������.
     * <p>
     * import aaa.bbb.ccc.DDD�G // new AvailableNamespace({"aaa","bbb","ccc","DDD"}, false); <br>
     * import aaa.bbb.ccc.*; // new AvailableNamespace({"aaa","bbb","ccc"},true); <br>
     * </p>
     * 
     * @param namespace ���p�\���O��Ԗ�
     * @param allClasses �S�ẴN���X�����p�\���ǂ���
     */
    public UnresolvedClassImportStatementInfo(final String[] namespace, final boolean allClasses) {
        super(namespace, allClasses);
    }

    @Override
    public ClassImportStatementInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // fix import name to original text
        final String[] originalImportName = this.getImportName();
        String[] importName;
        if (this.isAll()) {
            final int length = originalImportName.length;
            importName = new String[length + 1];
            System.arraycopy(originalImportName, 0, importName, 0, length);
            importName[length] = "*";
        } else {
            importName = originalImportName;
        }
        this.resolvedInfo = new ClassImportStatementInfo(importName, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * ���O��Ԗ���Ԃ��D
     * 
     * @return ���O��Ԗ�
     */
    public String[] getNamespace() {

        final String[] importName = this.getImportName();
        if (this.isAll()) {
            return importName;
        }

        final String[] namespace = new String[importName.length - 1];
        System.arraycopy(importName, 0, namespace, 0, importName.length - 1);
        return namespace;
    }
}
