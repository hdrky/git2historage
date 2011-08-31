package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.TypeBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * Java�p�̌^�\�z���s���r���_�[�D
 * �W�F�l���N�X�̌^�����Ń��C���h�J�[�h�g�p���ɁC�e�N���X���w�肳��Ȃ������ꍇ�ɁC
 * java.lang.Object��o�^����D
 * 
 * @author kou-tngt
 *
 */
public class JavaTypeBuilder extends TypeBuilder {

    /**
     * �e�N���X�̓����������󂯎��R���X�g���N�^���Ăяo���D
     * 
     * @param buildDataManager
     */
    public JavaTypeBuilder(final BuildDataManager buildDataManager) {
        super(buildDataManager);
    }

    /**
     * ���C���h�J�[�h�g�p���ɋ��E�Ƃ��Ďw�肳�ꂽ�^��Ԃ��D
     * Java�̏ꍇ�w�肳��Ȃ����java.lang.Object��Ԃ��D
     * @return ����N���X�Ƃ��Ďw�肳�ꂽ�^���C�w�肳��Ȃ����java.lang.Object��Ԃ��D
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.TypeBuilder#getCurrentUpperBounds()
     */
    @Override
    protected UnresolvedTypeInfo<? extends TypeInfo> getCurrentBounds() {
        final UnresolvedTypeInfo<? extends TypeInfo> type = super.getCurrentBounds();
        if (null == type) {
            return JAVA_LANG_OBJECT;
        } else {
            return type;
        }
    }

    /**
     * java.lang.Object��\���^�Q��
     */
    public final static UnresolvedClassTypeInfo JAVA_LANG_OBJECT = new UnresolvedClassTypeInfo(
            new LinkedList<UnresolvedClassImportStatementInfo>(), new String[] { "java", "lang",
                    "Object" });

    public final static UnresolvedClassTypeInfo JAVA_LANG_STRIG = new UnresolvedClassTypeInfo(
            new LinkedList<UnresolvedClassImportStatementInfo>(), new String[] { "java", "lang",
                    "String" });
}
