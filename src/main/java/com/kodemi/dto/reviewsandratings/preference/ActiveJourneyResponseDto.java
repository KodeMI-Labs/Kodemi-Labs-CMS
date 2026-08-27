package com.kodemi.dto.reviewsandratings.preference;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The single API response consumed by the Learner frontend to render the entire
 * onboarding flow dynamically.
 *
 * The frontend receives this JSON and: 1. Iterates pages in page_order
 * sequence. 2. Renders each page according to its field_type and
 * selection_type. 3. Displays options for that page (with icons where
 * available). 4. Applies rules to determine the next page after a selection.
 *
 * The frontend never hardcodes screen count, option labels, or navigation.
 * Everything is driven entirely by this response.
 *
 * Example (from UI mockups):
 *
 * Page 1 – title: "Choose Profession", field_type: ICON_GRID, searchable: true
 * Options: Business | Designing | IT & Software | Accounting | ...
 *
 * Page 2 – title: "Preferred Skills", field_type: RADIO_LIST, searchable: true
 * Options: Design skills | Technical skills | Data & AI skills | ...
 *
 * Page 3 – title: "Choose Your Learning Path", field_type: RADIO_LIST,
 * searchable: true Options: UI UX | Motion Graphics Design | Game Design | ...
 *
 * Page 4 – title: "Preferred Skills", field_type: SKILL_LEVEL Options: Beginner
 * | Intermediate | Advanced + Learning Goal dropdown
 */
@Getter
@Setter
public class ActiveJourneyResponseDto {

	// ── Journey-level metadata ────────────────────────────────────────────────
	private String journey_id;
	private String journey_name;
	private String description;
	private Integer version;
	private LocalDateTime publish_date;

	// ── Ordered list of pages ─────────────────────────────────────────────────
	private List<PageWithOptions> pages;

	// ── Navigation rules (sent so the frontend can resolve next-page locally) ─
	private List<PreferenceRuleDto> rules;

	// ─────────────────────────────────────────────────────────────────────────
	// Nested: one page with its options already embedded
	// ─────────────────────────────────────────────────────────────────────────
	@Getter
	@Setter
	public static class PageWithOptions {

		private String page_id;
		private Integer page_order;

		private String title;
		private String subtitle;
		private String description;

		// Tells the frontend which component to render:
		// ICON_GRID → grid of icon cards (screen 1 in mockup)
		// RADIO_LIST → radio button list (screens 2 & 3 in mockup)
		// CHECKBOX_LIST→ checkbox list
		// DROPDOWN → select / combobox (Learning Goal in screen 4)
		// SKILL_LEVEL → Beginner/Intermediate/Advanced row (screen 4)
		// TEXT_INPUT → free text
		private String field_type;

		// SINGLE | MULTI
		private String selection_type;

		// If true, render a search bar above the options list
		private Boolean searchable;

		// If true, the "Skip For Now" button is hidden
		private Boolean mandatory;

		// Default next page (may be overridden by a rule)
		private String next_page_id;

		// All active options for this page, sorted by display_order
		private List<OptionSummary> options;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Nested: lightweight option summary (only what the frontend needs)
	// ─────────────────────────────────────────────────────────────────────────
	@Getter
	@Setter
	public static class OptionSummary {

		private String option_id;

		// Text label, e.g. "IT & Software"
		private String display_name;

		// Machine-readable value sent back in the learner's answer payload
		private String value;

		// Circular icon URL shown on ICON_GRID pages (null for RADIO_LIST pages)
		private String icon_url;

		// Larger image card URL (optional)
		private String image_url;

		private Integer display_order;

		// Parent option this item belongs to (for Phase 3 child options)
		private String parent_option_id;

		// Option-level next-page override (null = use page default)
		private String next_page_id;
	}
}
