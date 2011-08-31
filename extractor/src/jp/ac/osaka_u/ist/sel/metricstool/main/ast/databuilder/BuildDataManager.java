package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


/**
 * �r���_�[���\�z��������Ǘ����āC���S�̂̐����������C���^�t�F�[�X�D
 * �ȉ���3��ނ̋@�\��A�g���čs��.
 * 
 * 1. �\�z���̃f�[�^�Ɋւ�����̊Ǘ��C�񋟋y�э\�z��Ԃ̊Ǘ�
 * 
 * 2. ���O��ԁC�G�C���A�X�C�ϐ��Ȃǂ̃X�R�[�v�Ǘ�
 * 
 * 3. �N���X���C���\�b�h���C�ϐ�����C�ϐ��Q�ƁC���\�b�h�Ăяo�����Ȃǂ̓o�^��Ƃ̑�s
 * 
 * @author kou-tngt
 *
 */
public interface BuildDataManager {

    /**
     * �\�z���̃N���X�Ƀt�B�[���h����ǉ�����
     * 
     * @param field
     */
    public void addField(UnresolvedFieldInfo field);

    /**
     * �\�z���̃��\�b�h�Ƀt�B�[���h�g�p����ǉ�����
     * 
     * @param usage
     */
    public void addVariableUsage(
            UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usage);

    /**
     * �\�z���̃��\�b�h�Ƀ��[�J���p�����[�^�ifor�����Ő錾�����ϐ��̂悤�ɁC
     * �錾���ꂽ�ꏊ���玟�̃u���b�N�̏I���܂ŃX�R�[�v���L���ȕϐ��j����ǉ�����
     * 
     * @param localParameter
     */
    public void addLocalParameter(UnresolvedLocalVariableInfo localParameter);

    /**
     * �\�z���̃��\�b�h�Ƀ��[�J���ϐ�����ǉ�����
     * 
     * @param localVariable
     */
    public void addLocalVariable(UnresolvedLocalVariableInfo localVariable);

    /**
     * �\�z���̃��\�b�h�Ƀ��\�b�h�Ăяo������ǉ�����
     * @param memberCall
     */
    public void addMethodCall(
            UnresolvedCallInfo<? extends CallInfo<? extends CallableUnitInfo>> memberCall);

    /**
     * �\�z���̃��\�b�h�Ɉ�������ǉ�����
     * 
     * @param parameter
     */
    public void addMethodParameter(UnresolvedParameterInfo parameter);

    /**
     * �\�z���̃f�[�^�ɓK�؂Ɍ^�p�����[�^���Z�b�g����D
     * @param typeParameter�@�Z�b�g����^�p�����[�^
     */
    public void addTypeParameger(UnresolvedTypeParameterInfo typeParameter);

    /**
     * ���݂̃u���b�N�X�R�[�v���ŗL���Ȗ��O�G�C���A�X��ǉ�����
     * 
     * @param alias �C���|�[�g���ɂ��u��������ꂽ���O 
     * @param importStatement �������C���|�[�g��
     */
    public void addUsingAlias(String alias, final UnresolvedImportStatementInfo<?> importStatement);

    /**
     * ���݂̃u���b�N�X�R�[�v���ŗL���ȁC���O��ԗ��p����ǉ�����
     * 
     * @param importStatement �������C���|�[�g��
     */
    public void addUsingNameSpace(final UnresolvedImportStatementInfo<?> importStatement);

    public void addStatement(final UnresolvedStatementInfo<? extends StatementInfo> statement);

    public void addLabel(final UnresolvedLabelInfo label);

    public UnresolvedLabelInfo getAvailableLabel(final String labelName);

    /**
     * �N���X�\�z���I�����鎞�ɌĂ΂��D
     * �\�z����
     * 
     * @return
     */
    public UnresolvedClassInfo endClassDefinition();

    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> endCallableUnitDefinition();

    public UnresolvedBlockInfo<? extends BlockInfo> endInnerBlockDefinition();

    public void endScopedBlock();

    public void enterClassBlock();

    public void enterMethodBlock();

    public List<UnresolvedImportStatementInfo<?>> getAvailableNameSpaceSet();

    public List<UnresolvedImportStatementInfo<?>> getAvailableAliasSet();

    public List<UnresolvedImportStatementInfo<?>> getAllAvaliableNames();

    public String[] getAliasedName(String name);

    public int getAnonymousClassCount(UnresolvedClassInfo classInfo);

    public UnresolvedUnitInfo<? extends UnitInfo> getCurrentUnit();
    
    public UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit();
    
    public UnresolvedClassInfo getCurrentClass();

    public String[] getCurrentNameSpace();

    public UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> getCurrentLocalSpace();

    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getCurrentCallableUnit();

    public UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> getCurrentScopeVariable(
            String name);

    public UnresolvedTypeParameterTypeInfo getTypeParameterType(String name);

    public boolean hasAlias(String name);

    public void reset();

    public String[] popNameSpace();

    public void pushNewNameSpace(String[] nameSpace);

    public String[] resolveAlias(String[] name);

    public void startScopedBlock();

    public void startClassDefinition(UnresolvedClassInfo classInfo);

    public void startCallableUnitDefinition(
            UnresolvedCallableUnitInfo<? extends CallableUnitInfo> methodInfo);

    public void startInnerBlockDefinition(UnresolvedBlockInfo<? extends BlockInfo> blockInfo);

    public UnresolvedBlockInfo<? extends BlockInfo> getCurrentBlock();

    public int getCurrentTypeParameterCount();

    public int getCurrentParameterCount();

}
