package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * {@link BuildDataManager}�ł̃u���b�N��(�N���X�C���\�b�h�C�u���b�N)���Ǘ����邽�߂̃X�^�b�N
 * �N���X�C���\�b�h�C�u���b�N���ꂼ�����ʂ����ɃX�^�b�N�ɐςނق��C�֋X�セ�ꂼ��̃X�^�b�N���ێ����Ă���D
 * @author g-yamada
 *
 */
public final class UnitStack extends Stack<UnresolvedUnitInfo<? extends UnitInfo>> {

    /**
     * �R���X�g���N�^
     */
    public UnitStack() {
        super();
        this.classStack = new Stack<UnresolvedClassInfo>();
        this.callableUnitStack = new Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>>();
        this.blockStack = new Stack<UnresolvedBlockInfo<? extends BlockInfo>>();
    }

    /**
     * ���g�̃X�^�b�N����v�f���ЂƂ|�b�v���C�|�b�v�����v�f�ɉ����đΉ�����ʂ̃X�^�b�N���|�b�v����
     */
    @Override
    public UnresolvedUnitInfo<? extends UnitInfo> pop() {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();

        final UnresolvedUnitInfo<? extends UnitInfo> result = super.pop();

        if (result instanceof UnresolvedBlockInfo) {
            this.blockStack.pop();
        } else if (result instanceof UnresolvedCallableUnitInfo) {
            this.callableUnitStack.pop();
        } else if (result instanceof UnresolvedClassInfo) {
            this.classStack.pop();
        } else {
            assert false : "here should not be reached";
        }

        return result;
    }

    /**
     * �N���X���X�^�b�N�ɐς�
     * @param unit ���̃X�^�b�N�ɐςރN���X
     */
    public void push(final UnresolvedClassInfo unit) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unit) {
            throw new IllegalArgumentException();
        }
        super.push(unit);
        this.classStack.push(unit);
    }

    /**
     * �Ăяo���\�ȗv�f(���\�b�h�C�R���X�g���N�^)���X�^�b�N�ɐς�
     * @param unit ���̃X�^�b�N�ɐςތĂяo���\�ȗv�f
     */
    public void push(final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> unit) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unit) {
            throw new IllegalArgumentException();
        }
        super.push(unit);
        this.callableUnitStack.push(unit);
    }

    /**
     * �u���b�N���X�^�b�N�ɐς�
     * @param unit ���̃X�^�b�N�ɐςރu���b�N
     */
    public void push(final UnresolvedBlockInfo<? extends BlockInfo> unit) {
        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unit) {
            throw new IllegalArgumentException();
        }
        super.push(unit);
        this.blockStack.push(unit);
    }
    
    public void clear(){
        super.clear();
        this.classStack.clear();
        this.blockStack.clear();
        this.callableUnitStack.clear();
    }

    /**
     * ���߂̃N���X���擾����D�X�^�b�N����|�b�v���Ȃ�
     * @return ���߂̃N���X
     */
    public UnresolvedClassInfo getLatestClass() {
        return this.classStack.isEmpty() ? null : this.classStack.peek();
    }

    /**
     * ���߂̌Ăяo���\�ȗv�f(���\�b�h/�R���X�g���N�^)���擾����D�X�^�b�N����|�b�v���Ȃ��D
     * @return ���߂̌Ăяo���\�ȗv�f
     */
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getLatestCallableUnit() {
        return this.callableUnitStack.isEmpty() ? null : this.callableUnitStack.peek();
    }

    /**
     * ���߂̃u���b�N���擾����D�X�^�b�N����|�b�v���Ȃ�
     * @return ���߂̃u���b�N
     */
    public UnresolvedBlockInfo<? extends BlockInfo> getLatestBlock() {
        return this.blockStack.isEmpty() ? null : this.blockStack.peek();
    }

    /**
     * �N���X���X�^�b�N�̍ł���ɂ��邩�ǂ�����Ԃ�
     * @return �N���X���X�^�b�N�̍ł���ɂ����true�C�����łȂ����false
     */
    public boolean isClassAtPeek() {
        return this.peek() == this.getLatestClass();
    }

    /**
     * �u���b�N���X�^�b�N�̍ł���ɂ��邩�ǂ�����Ԃ�
     * @return �u���b�N���X�^�b�N�̍ł���ɂ����true, �����łȂ����false
     */
    public boolean isBlockAtPeek() {
        return this.peek() == this.getLatestBlock();
    }

    /**
     * �Ăяo���\�ȗv�f���X�^�b�N�̍ł���ɂ��邩�ǂ�����Ԃ�
     * @return �Ăяo���\�ȗv�f���X�^�b�N�̍ł���ɂ����true, �����łȂ����false
     */
    public boolean isCallableUnitAtPeek() {
        return this.peek() == this.getLatestCallableUnit();
    }
    
    /**
     * �N���X�݂̂��Ǘ����Ă���X�^�b�N���擾����
     * @return �N���X�݂̂��Ǘ����Ă���X�^�b�N
     */
    public Stack<UnresolvedClassInfo> getClassStack(){
        return this.classStack;
    }
    
    /**
     * �Ăяo���\�ȗv�f�݂̂��Ǘ����Ă���X�^�b�N���擾����
     * @return �Ăяo���\�ȗv�f�݂̂��Ǘ����Ă���X�^�b�N
     */
    public Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> getCallableUnitStack(){
        return this.callableUnitStack;
    }

    // ���̂Ƃ���K�v�Ȃ�
//    public Stack<UnresolvedBlockInfo<? extends BlockInfo>> getBlockStack(){};
    
    /**
     * �N���X�݂̂��Ǘ�����X�^�b�N
     */
    private final Stack<UnresolvedClassInfo> classStack;

    /**
     * �Ăяo���\�ȗv�f�݂̂��Ǘ�����N���X
     */
    private final Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> callableUnitStack;

    /**
     * �u���b�N�݂̂��Ǘ�����N���X
     */
    private final Stack<UnresolvedBlockInfo<? extends BlockInfo>> blockStack;

    /**
     * �������������V���A��ID
     */
    private static final long serialVersionUID = 3545194400868254302L;
}
