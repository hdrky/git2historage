package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StaticInitializerStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStaticInitializerInfo;


/**
 * �X�^�e�B�b�N�C�j�V�����C�U�̃r���_
 * 
 * @author g-yamada
 *
 */
public class StaticInitializerBuilder extends InitializerBuilder<UnresolvedStaticInitializerInfo> {

    public StaticInitializerBuilder(BuildDataManager buildDataManager) {
        super(buildDataManager, new StaticInitializerStateManager());
    }

    @Override
    protected final void registToOwnerClass(final UnresolvedStaticInitializerInfo initializer) {
        initializer.getOwnerClass().addStaticInitializer(initializer);
    }

    @Override
    protected final UnresolvedStaticInitializerInfo createUnresolvedCallableUnitInfo(int fromLine,
            int fromColumn, int toLine, int toColumn) {
        return new UnresolvedStaticInitializerInfo(this.buildManager.getCurrentClass(), fromLine,
                fromColumn, toLine, toColumn);
    }
}
