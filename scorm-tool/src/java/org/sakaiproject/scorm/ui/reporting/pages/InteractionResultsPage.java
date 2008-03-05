package org.sakaiproject.scorm.ui.reporting.pages;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.sakaiproject.scorm.model.api.ContentPackage;
import org.sakaiproject.scorm.model.api.Interaction;
import org.sakaiproject.scorm.model.api.Learner;
import org.sakaiproject.scorm.ui.reporting.components.InteractionPanel;
import org.sakaiproject.scorm.ui.reporting.util.ObjectiveProvider;
import org.sakaiproject.wicket.markup.html.link.BookmarkablePageLabeledLink;
import org.sakaiproject.wicket.markup.html.repeater.data.presenter.EnhancedDataPresenter;

public class InteractionResultsPage extends BaseResultsPage {

	private static final long serialVersionUID = 1L;

	
	
	public InteractionResultsPage(PageParameters pageParams) {
		super(pageParams);
	}
	
	@Override
	protected void initializePage(ContentPackage contentPackage,
			Learner learner, long attemptNumber, PageParameters pageParams) {
		String scoId = pageParams.getString("scoId");
		
		PageParameters uberuberparentParams = new PageParameters();
		uberuberparentParams.put("contentPackageId", contentPackage.getContentPackageId());
		
		PageParameters parentParams = new PageParameters();
		parentParams.put("contentPackageId", contentPackage.getContentPackageId());
		parentParams.put("learnerId", learner.getId());
		parentParams.put("attemptNumber", attemptNumber);
		
		String interactionId = pageParams.getString("interactionId");
		
		Interaction interaction = resultService.getInteraction(contentPackage.getContentPackageId(), learner.getId(), attemptNumber, scoId, interactionId);
		
		add(new InteractionPanel("interactionPanel", interaction));
		
		IModel breadcrumbModel = new StringResourceModel("uberuberparent.breadcrumb", this, new Model(contentPackage));
		addBreadcrumb(breadcrumbModel, ResultsListPage.class, uberuberparentParams, true);	
		addBreadcrumb(new Model(learner.getDisplayName()), LearnerResultsPage.class, parentParams, true);
		addBreadcrumb(new Model(interaction.getActivityTitle()), ScoResultsPage.class, pageParams, true);
		addBreadcrumb(new Model(interaction.getInteractionId()), InteractionResultsPage.class, pageParams, false);
		
		ObjectiveProvider dataProvider = new ObjectiveProvider(interaction.getObjectives());
		dataProvider.setTableTitle("Objectives");
		EnhancedDataPresenter presenter = new EnhancedDataPresenter("objectivePresenter", getColumns(), dataProvider);
		add(presenter);	

	}

	@Override
	protected BookmarkablePageLabeledLink newAttemptNumberLink(long i, PageParameters params) {
		return new BookmarkablePageLabeledLink("attemptNumberLink", new Model("" + i), InteractionResultsPage.class, params);
	}

	private List<IColumn> getColumns() {
		IModel idHeader = new ResourceModel("column.header.id");
		IModel descriptionHeader = new ResourceModel("column.header.description");
		IModel completionStatusHeader = new ResourceModel("column.header.completion.status");
		IModel successStatusHeader = new ResourceModel("column.header.success.status");
		
		List<IColumn> columns = new LinkedList<IColumn>();

		columns.add(new PropertyColumn(idHeader, "id", "id"));
		columns.add(new PropertyColumn(descriptionHeader, "description", "description"));
		columns.add(new PropertyColumn(completionStatusHeader, "completionStatus", "completionStatus"));
		columns.add(new PropertyColumn(successStatusHeader, "successStatus", "successStatus"));
		
		return columns;
	}
	
}