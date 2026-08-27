package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class IncomeIdeaCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val shortBadge: String
) {
    SKILLS_BASED(
        id = "skills",
        title = "Skills-Based",
        subtitle = "Freelancing, Specialized Consulting, Executive Advisory & Coaching",
        shortBadge = "SKILLS"
    ),
    PRODUCT_BASED(
        id = "product",
        title = "Product-Based",
        subtitle = "Digital Knowledge, Micro-SaaS, E-Commerce & Media Systems",
        shortBadge = "PRODUCT"
    ),
    INVESTMENT_BASED(
        id = "investment",
        title = "Investment-Based",
        subtitle = "Cash-Flow Portfolios, Real Estate Basics & Automated Royalty Vehicles",
        shortBadge = "INVEST"
    )
}

enum class EffortLevel(
    val label: String,
    val description: String
) {
    LOW(
        label = "Low Effort",
        description = "Rapid setup, minimal upfront capital, relies on existing skill assets"
    ),
    MEDIUM(
        label = "Medium Effort",
        description = "Requires systematic process setup, moderate content or client pipeline building"
    ),
    HIGH(
        label = "High Effort",
        description = "Demands significant initial capitalization, technical architecture, or deep asset acquisition"
    )
}

data class IncomeIdea(
    val id: String,
    val title: String,
    val category: IncomeIdeaCategory,
    val tagLine: String,
    val briefExplainer: String,
    val effortLevel: EffortLevel,
    val capitalRequired: String,
    val timeToFirstRevenue: String,
    val scalabilityRating: String,
    val linkedModuleId: Int,
    val linkedPrinciple: String,
    val linkedModuleTitle: String,
    val linkedPrincipleRationale: String,
    val keySteps: List<String>,
    val prerequisites: List<String>,
    val pros: List<String>,
    val considerations: List<String>,
    val notebookPrompt: String
)

@Entity(tableName = "saved_income_ideas")
data class SavedIncomeIdeaEntity(
    @PrimaryKey val ideaId: String,
    val savedAtEpoch: Long = System.currentTimeMillis(),
    val notes: String = ""
)

object IncomeIdeaLibraryData {

    val allCategories = listOf(
        IncomeIdeaCategory.SKILLS_BASED,
        IncomeIdeaCategory.PRODUCT_BASED,
        IncomeIdeaCategory.INVESTMENT_BASED
    )

    val ideas: List<IncomeIdea> = listOf(
        // ==========================================
        // 1. SKILLS-BASED CATEGORY
        // ==========================================
        IncomeIdea(
            id = "skills_b2b_consulting",
            title = "B2B Specialized Systems Consulting",
            category = IncomeIdeaCategory.SKILLS_BASED,
            tagLine = "Package deep domain knowledge into high-ticket enterprise audits and workflows.",
            briefExplainer = "Identify a specific bottleneck in corporate workflows (operations, marketing funnels, sales automation, or compliance) and offer structured advisory audits. Requires deep domain fluency, client outreach, and delivering measurable ROI rather than billing generic hourly labor.",
            effortLevel = EffortLevel.LOW,
            capitalRequired = "$0 – $250 (Website & Outreach Tools)",
            timeToFirstRevenue = "2 – 6 Weeks",
            scalabilityRating = "High (Can transition into agency or productized audit)",
            linkedModuleId = 4,
            linkedPrinciple = "Specialized Knowledge",
            linkedModuleTitle = "Module 4: Specialized Knowledge",
            linkedPrincipleRationale = "General knowledge attracts ordinary compensation. By concentrating your understanding into an acute specialized niche that solves costly business problems, you command premium retainers.",
            keySteps = listOf(
                "Define your singular niche: pinpoint 1 painful business problem you can definitively resolve.",
                "Craft an offer sheet detailing exact deliverables, timeline, and projected ROI for the client.",
                "Initiate personalized outreach to 30 targeted decision-makers with a free mini-diagnostic audit.",
                "Execute the engagement with thorough documentation, gathering a case study and referral pledge."
            ),
            prerequisites = listOf(
                "3+ years of practical domain experience in a specific vertical",
                "Clear verbal & written executive communication",
                "Basic understanding of client discovery and proposal pitching"
            ),
            pros = listOf(
                "Near-zero capital required to initiate",
                "High profit margins (90%+)",
                "Immediate cash flow upon contract signing"
            ),
            considerations = listOf(
                "Directly exchanges focused cognitive time for income initially",
                "Requires proactive pipeline development and client negotiation"
            ),
            notebookPrompt = "What specialized domain skill do you possess that could save an enterprise $50,000 or 100 hours of wasted effort?"
        ),
        IncomeIdea(
            id = "skills_fractional_executive",
            title = "Fractional Executive & Strategy Advisory",
            category = IncomeIdeaCategory.SKILLS_BASED,
            tagLine = "Serve as a part-time COO, CMO, or CTO for growing mid-market enterprises.",
            briefExplainer = "Instead of taking a single full-time corporate role, partner with 2 to 4 growing companies for 10-15 hours per week each. Provide executive leadership, strategic roadmaps, and team oversight at a fraction of full-time hiring costs.",
            effortLevel = EffortLevel.MEDIUM,
            capitalRequired = "$100 – $500 (Brand presence & legal contracts)",
            timeToFirstRevenue = "4 – 8 Weeks",
            scalabilityRating = "Moderate (High revenue ceiling per client, capped by personal hours)",
            linkedModuleId = 9,
            linkedPrinciple = "The Master Mind",
            linkedModuleTitle = "Module 9: The Master Mind",
            linkedPrincipleRationale = "Napoleon Hill taught that no single individual has sufficient experience or ability without the coordination of other minds. A fractional executive functions as the strategic mastermind multiplier for growing businesses.",
            keySteps = listOf(
                "Package your executive track record into a 1-page strategic scorecard.",
                "Network within founder circles and venture portfolios seeking interim leadership.",
                "Structure monthly retainer agreements with clear milestone reviews and autonomy boundaries.",
                "Build leverage by instituting standard operating procedures for the client's internal team."
            ),
            prerequisites = listOf(
                "Track record of leadership, team management, or P&L responsibility",
                "Strong strategic thinking and multi-project prioritization"
            ),
            pros = listOf(
                "Predictable, recurring multi-thousand dollar monthly retainers",
                "Diversified income across multiple non-competing clients",
                "High intellectual stimulation and sovereignty over your calendar"
            ),
            considerations = listOf(
                "High mental bandwidth needed to juggle multiple corporate contexts",
                "Demands emotional intelligence to lead teams you do not manage full-time"
            ),
            notebookPrompt = "How can you position your strategic perspective as a fractional mastermind advisor to founders who need executive guidance?"
        ),
        IncomeIdea(
            id = "skills_high_ticket_copywriting",
            title = "High-Conversion Direct-Response Copywriting",
            category = IncomeIdeaCategory.SKILLS_BASED,
            tagLine = "Write sales letters, email funnels, and landing pages that drive revenue.",
            briefExplainer = "Direct-response copywriting is the art and science of persuasive written communication that compels action. Businesses gladly pay substantial upfront fees plus revenue royalties to copywriters who can directly increase conversion rates.",
            effortLevel = EffortLevel.LOW,
            capitalRequired = "$0 – $100 (Portfolio hosting & writing tools)",
            timeToFirstRevenue = "2 – 4 Weeks",
            scalabilityRating = "High (Can negotiate performance royalties on backend sales)",
            linkedModuleId = 1,
            linkedPrinciple = "Desire",
            linkedModuleTitle = "Module 1: The Ignition (Desire)",
            linkedPrincipleRationale = "Master copywriters understand the deep subconscious desires and emotional triggers that drive human decisions. Channeling raw desire into definitive action is the core of commercial transmutation.",
            keySteps = listOf(
                "Study classic direct-response frameworks (AIDA, PAS, Offer Architecture).",
                "Deconstruct 10 winning sales pages in a specific niche (e.g., SaaS, FinTech, Health).",
                "Build a 3-specimen portfolio demonstrating clear before/after conversion improvements.",
                "Offer a risk-free headline/email overhaul to 5 qualified businesses to generate immediate testimonials."
            ),
            prerequisites = listOf(
                "Deep empathy for consumer psychology and buying triggers",
                "Clear, punchy, persuasive writing ability",
                "Basic analytics fluency (measuring open rates, click-throughs, and conversions)"
            ),
            pros = listOf(
                "Can be done 100% remotely from anywhere in the world",
                "Potential for backend royalty upside (% of sales generated)",
                "Skills compound and make you lethal in every other business venture"
            ),
            considerations = listOf(
                "Performance-driven accountability: copy must actually generate sales",
                "High competition in low-end freelance marketplaces (must position high-end)"
            ),
            notebookPrompt = "What market or industry do you understand so deeply that you can speak directly to their hidden desires and frustrations?"
        ),
        IncomeIdea(
            id = "skills_executive_coaching",
            title = "High-Performance Executive & Mindset Coaching",
            category = IncomeIdeaCategory.SKILLS_BASED,
            tagLine = "Guide leaders and creators through mental hurdles, accountability, and peak performance.",
            briefExplainer = "Provide structured 1-on-1 and small-group coaching for entrepreneurs, executives, or professionals aiming to break plateaus. Focus on discipline frameworks, psychological resilience, habit systems, and goal realization protocols.",
            effortLevel = EffortLevel.LOW,
            capitalRequired = "$50 – $300 (Video conferencing & booking software)",
            timeToFirstRevenue = "1 – 3 Weeks",
            scalabilityRating = "Moderate (1-on-1 has hourly limits; scalable via mastermind cohorts)",
            linkedModuleId = 2,
            linkedPrinciple = "Faith",
            linkedModuleTitle = "Module 2: Unshakeable Belief (Faith)",
            linkedPrincipleRationale = "Hill identified that self-limiting doubt paralyzes the human faculty of achievement. A master coach instills systematic faith and accountability until the client internalizes sovereign confidence.",
            keySteps = listOf(
                "Develop a proprietary 90-day transformation framework with weekly milestones.",
                "Host 3 pro-bono diagnostic breakthrough sessions to calibrate your methodology.",
                "Gather video testimonials and establish an application-only onboarding pipeline.",
                "Offer tiered packages (e.g., 3-month intensive, bi-weekly advisory, executive hotline)."
            ),
            prerequisites = listOf(
                "Demonstrated track record of personal achievement or psychological mastery",
                "Active listening, non-judgmental empathy, and constructive candor",
                "Structured accountability tracking system"
            ),
            pros = listOf(
                "Profound personal fulfillment seeing clients break multi-year plateaus",
                "High client retention when tangible milestones are consistently achieved",
                "Extremely low operating overhead"
            ),
            considerations = listOf(
                "Requires constant emotional grounding and boundary management",
                "Reputation is paramount; requires genuine commitment to client outcomes"
            ),
            notebookPrompt = "What personal breakthrough or transformation have you navigated that you can systematically guide others through?"
        ),
        IncomeIdea(
            id = "skills_technical_architecture",
            title = "AI Workflow & Automation Integration",
            category = IncomeIdeaCategory.SKILLS_BASED,
            tagLine = "Architect automated operational pipelines and AI copilots for small-to-medium businesses.",
            briefExplainer = "Most traditional businesses waste hundreds of hours manually copying data, answering repetitive customer inquiries, and managing invoices. By designing custom automated pipelines (using APIs, LLMs, and low-code connectors), you eliminate overhead for clients.",
            effortLevel = EffortLevel.MEDIUM,
            capitalRequired = "$100 – $400 (API sandboxes & automation platform tiers)",
            timeToFirstRevenue = "3 – 6 Weeks",
            scalabilityRating = "High (Can bundle setup fees with ongoing maintenance retainers)",
            linkedModuleId = 6,
            linkedPrinciple = "Organized Planning",
            linkedModuleTitle = "Module 6: Organized Planning",
            linkedPrincipleRationale = "Organized planning transforms vague intentions into exact, automated operational engines that function flawlessly without manual intervention.",
            keySteps = listOf(
                "Master modern workflow integration platforms and LLM API tool calling.",
                "Document a standard '10-Hour Automation Audit' for target verticals (e.g., law firms, real estate agencies).",
                "Deliver a working proof-of-concept workflow in 48 hours for an initial flagship client.",
                "Package monthly support retainers for pipeline monitoring and optimization."
            ),
            prerequisites = listOf(
                "Logical problem-solving and systems-thinking mindset",
                "Familiarity with API webhooks, JSON structures, or modern automation tools",
                "Ability to map complex business processes into sequential steps"
            ),
            pros = listOf(
                "Massive current market demand with low supply of skilled integrators",
                "Dual revenue stream: High implementation fee + monthly maintenance retainer",
                "High client switching costs once your automations power their core business"
            ),
            considerations = listOf(
                "Requires staying updated on rapid technological shifts in AI and API platforms",
                "Must build robust error-handling so broken third-party APIs do not halt client operations"
            ),
            notebookPrompt = "What repetitive, manual operational process have you seen in businesses that could be completely automated?"
        ),

        // ==========================================
        // 2. PRODUCT-BASED CATEGORY
        // ==========================================
        IncomeIdea(
            id = "product_micro_saas",
            title = "Niche Micro-SaaS & Workflow Software",
            category = IncomeIdeaCategory.PRODUCT_BASED,
            tagLine = "Build a lightweight web or mobile software tool solving one acute problem for a specific niche.",
            briefExplainer = "Instead of attempting to build the next giant platform, micro-SaaS focuses on a single unserved workflow for a defined professional community (e.g., automated inventory forecasting for boutique roasters, or client intake automation for physiotherapists).",
            effortLevel = EffortLevel.HIGH,
            capitalRequired = "$100 – $1,000 (Cloud hosting, domains, auth & billing)",
            timeToFirstRevenue = "2 – 4 Months",
            scalabilityRating = "Extremely High (Software has zero marginal cost of replication)",
            linkedModuleId = 5,
            linkedPrinciple = "Imagination",
            linkedModuleTitle = "Module 5: Imagination",
            linkedPrincipleRationale = "Synthetic imagination takes existing tools and combines them into novel configurations that deliver frictionless utility to a hungry audience.",
            keySteps = listOf(
                "Validate demand by interviewing 15 practitioners in your target industry about daily friction.",
                "Build a Minimum Lovable Product (MLP) focusing strictly on the single core feature in 3-4 weeks.",
                "Pre-sell annual founding member access at a 50% discount to validate real buying commitment.",
                "Iterate based on active user analytics and institute automated onboarding."
            ),
            prerequisites = listOf(
                "Software engineering fluency or proficiency with modern full-stack AI development tools",
                "Deep understanding of the specific target user's workflow pain points",
                "Basic understanding of subscription metrics (MRR, churn, LTV)"
            ),
            pros = listOf(
                "High gross margins (80-90%) and predictable monthly recurring revenue (MRR)",
                "Creates a sellable enterprise asset with significant valuation multiples",
                "Works 24/7 without requiring direct time-for-money exchange"
            ),
            considerations = listOf(
                "Requires ongoing customer support, security maintenance, and bug fixes",
                "Initial development curve requires discipline before first dollar is earned"
            ),
            notebookPrompt = "What frustrating software friction do you experience in your daily workflow that thousands of others likely share?"
        ),
        IncomeIdea(
            id = "product_digital_knowledge",
            title = "Digital Knowledge Vault & Framework Systems",
            category = IncomeIdeaCategory.PRODUCT_BASED,
            tagLine = "Codify your expertise into comprehensive guides, masterclasses, and actionable frameworks.",
            briefExplainer = "Package specialized playbooks, video masterclasses, or tactical frameworks into downloadable digital knowledge products. Customers purchase structured shortcuts to achieve in weeks what took you years of trial and error to learn.",
            effortLevel = EffortLevel.MEDIUM,
            capitalRequired = "$50 – $200 (Platform hosting, checkout & recording gear)",
            timeToFirstRevenue = "3 – 6 Weeks",
            scalabilityRating = "Very High (Create once, sell indefinitely with zero inventory)",
            linkedModuleId = 4,
            linkedPrinciple = "Specialized Knowledge",
            linkedModuleTitle = "Module 4: Specialized Knowledge",
            linkedPrincipleRationale = "Specialized knowledge only becomes power when organized into definite plans of action. A structured digital curriculum is the literal organization of knowledge into marketable value.",
            keySteps = listOf(
                "Audit your unique career accomplishments: what specific milestone can you teach others?",
                "Outline the step-by-step curriculum with accompanying worksheets, templates, and checklists.",
                "Record high-clarity video walkthroughs or write a comprehensive tactical playbook.",
                "Build a high-converting landing page with social proof and launch to your audience or targeted traffic."
            ),
            prerequisites = listOf(
                "Documented results or specialized mastery in a given subject",
                "Ability to articulate complex processes in simple, digestible steps",
                "Basic video/audio recording or structured technical writing skills"
            ),
            pros = listOf(
                "100% gross profit on each digital sale after payment processing",
                "Builds immense personal authority and opens doors for speaking/consulting",
                "Can be updated continuously to increase lifetime customer value"
            ),
            considerations = listOf(
                "Requires an audience or targeted traffic acquisition strategy to scale sales",
                "Must deliver overwhelming practical utility to avoid refund requests and generate organic word of mouth"
            ),
            notebookPrompt = "What valuable skill did you struggle to learn that you could now teach with absolute clarity?"
        ),
        IncomeIdea(
            id = "product_curated_newsletter",
            title = "Niche Paid Newsletter & Private Member Community",
            category = IncomeIdeaCategory.PRODUCT_BASED,
            tagLine = "Curate high-signal industry intelligence and foster an exclusive peer network.",
            briefExplainer = "Publish weekly curated analysis, proprietary data breakdowns, or insider market intelligence for a specific niche (e.g., commercial real estate tech, supply chain innovation, or indie software). Monetize through paid subscriptions, sponsorships, and mastermind access.",
            effortLevel = EffortLevel.MEDIUM,
            capitalRequired = "$0 – $150 (Newsletter platform & community portal)",
            timeToFirstRevenue = "1 – 3 Months",
            scalabilityRating = "High (Content scales to 10,000+ readers with flat production cost)",
            linkedModuleId = 8,
            linkedPrinciple = "Persistence",
            linkedModuleTitle = "Module 8: Persistence",
            linkedPrincipleRationale = "Napoleon Hill emphasized that persistence is to character what carbon is to steel. Building an enduring, high-trust media asset requires relentless weekly consistency before momentum compounds.",
            keySteps = listOf(
                "Choose a tight, high-income niche where readers have significant commercial budget.",
                "Commit to a strict publishing cadence (e.g., Every Tuesday & Thursday at 8 AM).",
                "Deliver unmatched depth and curation that saves subscribers 5 hours of research each week.",
                "Introduce a premium tier with proprietary database access, private webinars, and member discussions."
            ),
            prerequisites = listOf(
                "Curiosity and dedication to continuous industry research",
                "Engaging writing voice that balances analytical rigor with brevity",
                "Consistency and discipline to never miss a scheduled edition"
            ),
            pros = listOf(
                "You own the direct distribution channel (email list is an independent asset)",
                "Diversified revenue: Subscriptions, premium sponsorships, job boards, and events",
                "High compounding trust and personal brand equity"
            ),
            considerations = listOf(
                "Requires a multi-month runway of consistent publishing before critical mass",
                "Must maintain consistently high editorial standards to prevent subscriber churn"
            ),
            notebookPrompt = "What industry topic do you find yourself constantly researching and discussing that needs a high-signal, fluff-free publication?"
        ),
        IncomeIdea(
            id = "product_digital_templates",
            title = "Operating System Templates & Process Blueprints",
            category = IncomeIdeaCategory.PRODUCT_BASED,
            tagLine = "Design turnkey productivity workspaces, financial models, and operational frameworks.",
            briefExplainer = "Build turnkey digital templates for platforms like Notion, Airtable, Figma, or Excel that solve complex organizational challenges (e.g., Venture Capital Due Diligence Tracker, Agency Client Portal, Real Estate Flipping Cash-Flow Model).",
            effortLevel = EffortLevel.LOW,
            capitalRequired = "$0 – $100 (Digital storefront & design assets)",
            timeToFirstRevenue = "1 – 3 Weeks",
            scalabilityRating = "High (Instant digital delivery with zero shipping or physical overhead)",
            linkedModuleId = 6,
            linkedPrinciple = "Organized Planning",
            linkedModuleTitle = "Module 6: Organized Planning",
            linkedPrincipleRationale = "Templates are organized planning made tangible. People happily pay for pre-structured systems that immediately replace chaos with order.",
            keySteps = listOf(
                "Identify complex, messy workflows people frequently complain about organizing.",
                "Build a comprehensive, beautifully designed template with embedded video tutorials.",
                "List on specialized template marketplaces and create short walkthrough demo videos.",
                "Bundle individual templates into complete 'Business Operating System' collections."
            ),
            prerequisites = listOf(
                "Advanced fluency in a popular productivity or modeling tool",
                "Clean visual hierarchy, UI design sensibility, and UX intuition",
                "Attention to detail in structuring data relationships and documentation"
            ),
            pros = listOf(
                "Extremely fast to build and publish (can be completed in a single weekend)",
                "Passive ongoing sales through marketplace search and social demonstration",
                "Serves as an effective low-friction lead generator for high-ticket consulting"
            ),
            considerations = listOf(
                "Low price point per unit requires high volume or bundling to generate substantial income",
                "Digital templates can be susceptible to unauthorized file sharing"
            ),
            notebookPrompt = "What organizational system or spreadsheet model have you built for yourself that other professionals would pay to duplicate?"
        ),
        IncomeIdea(
            id = "product_custom_ecommerce",
            title = "Specialty Niche E-Commerce & Print-on-Demand Brand",
            category = IncomeIdeaCategory.PRODUCT_BASED,
            tagLine = "Build an artisanal or lifestyle brand targeting passionate subcultures without inventory risk.",
            briefExplainer = "Create branded merchandise, custom art prints, specialized apparel, or curated physical products for a passionate community using modern on-demand manufacturing and drop-shipping logistics. Products are manufactured and shipped only after the customer pays.",
            effortLevel = EffortLevel.MEDIUM,
            capitalRequired = "$100 – $500 (Sample testing, store domain & initial marketing)",
            timeToFirstRevenue = "2 – 4 Weeks",
            scalabilityRating = "High (Manufacturing and fulfillment are fully outsourced)",
            linkedModuleId = 10,
            linkedPrinciple = "Transmutation",
            linkedModuleTitle = "Module 10: Transmutation",
            linkedPrincipleRationale = "Transmutation is the redirection of emotional energy and enthusiasm into creative physical manifestations. Building a beloved lifestyle brand harnesses cultural identity into commerce.",
            keySteps = listOf(
                "Select an underserved subculture with high identity attachment (e.g., vintage watch collectors, stoic philosophers, mechanical keyboard enthusiasts).",
                "Design distinctive, high-aesthetic graphics and product concepts.",
                "Order physical product samples to rigorously verify material and print quality.",
                "Launch storefront with lifestyle photography and engage with community influencers."
            ),
            prerequisites = listOf(
                "Visual design capability or ability to art-direct freelance designers",
                "Basic e-commerce store configuration and customer service setup",
                "Understanding of brand storytelling and social media marketing"
            ),
            pros = listOf(
                "Zero unsold inventory risk (pay for manufacturing only when an order is placed)",
                "Ability to test dozens of design concepts rapidly at zero extra marginal cost",
                "Can transition into custom bulk manufacturing once a design becomes a proven best-seller"
            ),
            considerations = listOf(
                "Lower profit margins compared to pure software or digital knowledge products",
                "Dependent on third-party shipping carriers and supplier quality control"
            ),
            notebookPrompt = "What passionate subculture or community do you belong to whose identity is poorly served by generic mainstream merchandise?"
        ),

        // ==========================================
        // 3. INVESTMENT-BASED CATEGORY
        // ==========================================
        IncomeIdea(
            id = "invest_dividend_index",
            title = "Core Dividend Growth & Index Cash-Flow Portfolio",
            category = IncomeIdeaCategory.INVESTMENT_BASED,
            tagLine = "Accumulate ownership in established global enterprises that pay rising quarterly distributions.",
            briefExplainer = "A foundational passive income vehicle centered on systematically buying diversified index funds and dividend aristocrats. Cash distributions are automatically reinvested during accumulation, then transitioned to passive cash flow for financial sovereignty.",
            effortLevel = EffortLevel.LOW,
            capitalRequired = "$100+ (Can start with fractional shares)",
            timeToFirstRevenue = "Immediate / Quarterly",
            scalabilityRating = "Infinite (Directly compounds with total capital allocated)",
            linkedModuleId = 0,
            linkedPrinciple = "Money / Wealth Context",
            linkedModuleTitle = "Module 0: The First Vault",
            linkedPrincipleRationale = "The First Vault establishes that true riches are measured by capital working tirelessly for you, rather than you perpetually working for capital. Compounding dividend reinvestment is mathematical alchemy.",
            keySteps = listOf(
                "Establish a dedicated, low-fee brokerage account with automated recurring deposit schedules.",
                "Construct a balanced core allocation (e.g., broad market index + dividend appreciation ETF).",
                "Enable Dividend Reinvestment Plans (DRIP) to maximize compounding velocity.",
                "Maintain emotional detachment and strictly dollar-cost average through all market volatility."
            ),
            prerequisites = listOf(
                "Surplus income from specialized skills or business operations to invest regularly",
                "Basic understanding of expense ratios, dividend yields, and long-term compound interest",
                "High emotional discipline to refrain from panic selling during economic contractions"
            ),
            pros = listOf(
                "Pure passive income: requires zero customer service, product development, or employee management",
                "High liquidity: shares can be converted to cash in standard market hours",
                "Favorable long-term capital gains and qualified dividend tax treatment in many jurisdictions"
            ),
            considerations = listOf(
                "Requires substantial capital accumulation to generate full living expenses from yields alone",
                "Market prices fluctuate; must maintain a multi-year horizon"
            ),
            notebookPrompt = "What percentage of your active income can you systematically allocate each month to purchase income-generating assets?"
        ),
        IncomeIdea(
            id = "invest_rental_real_estate",
            title = "Residential & Small Commercial Rental Cash-Flow Assets",
            category = IncomeIdeaCategory.INVESTMENT_BASED,
            tagLine = "Acquire cash-flowing real property using conservative leverage and professional management.",
            briefExplainer = "Real estate provides four distinct wealth generators simultaneously: monthly net rental cash flow, mortgage principal paydown by tenants, long-term property appreciation, and significant tax depreciation benefits.",
            effortLevel = EffortLevel.HIGH,
            capitalRequired = "$10,000 – $50,000+ (Down payment, reserves & closing costs)",
            timeToFirstRevenue = "1 – 3 Months post-acquisition",
            scalabilityRating = "High (Can refinance equity to acquire subsequent properties)",
            linkedModuleId = 7,
            linkedPrinciple = "Decision",
            linkedModuleTitle = "Module 7: Decision",
            linkedPrincipleRationale = "Napoleon Hill noted that master accumulators reach decisions promptly and change them slowly. Real estate acquisition requires decisive underwriting and steadfast commitment through long escrow cycles.",
            keySteps = listOf(
                "Establish your precise buy-box criteria (location, cap rate, cash-on-cash return target, property age).",
                "Underwrite 50 prospective properties with conservative vacancy, maintenance, and capital expenditure buffers.",
                "Secure pre-approval from commercial or conventional mortgage lenders.",
                "Partner with a vetted, top-tier local property management team to preserve passive operational status."
            ),
            prerequisites = listOf(
                "Good credit profile, stable baseline income, and liquid emergency reserves",
                "Proficiency in property cash-flow underwriting and financial modeling",
                "Basic knowledge of landlord-tenant laws and property insurance"
            ),
            pros = listOf(
                "Leverage: Control a $300,000 asset with $60,000 of equity capital",
                "Tangible, resilient physical asset with intrinsic shelter demand",
                "Substantial tax advantages via cost segregation and depreciation allowances"
            ),
            considerations = listOf(
                "Illiquid asset: selling requires time and closing friction",
                "Requires unexpected maintenance capital reserves for structural repairs"
            ),
            notebookPrompt = "What geographical markets or neighborhoods do you understand where rental demand remains consistently high?"
        ),
        IncomeIdea(
            id = "invest_digital_royalties",
            title = "Digital Intellectual Property & Media Licensing",
            category = IncomeIdeaCategory.INVESTMENT_BASED,
            tagLine = "Acquire or produce audio, visual, and code assets that generate perpetual licensing royalties.",
            briefExplainer = "Invest capital or upfront creative production into intellectual property assets—such as stock music tracks, 3D graphics libraries, algorithmic scripts, stock photography catalogs, or book publishing rights—that generate recurring royalty checks every time they are licensed.",
            effortLevel = EffortLevel.MEDIUM,
            capitalRequired = "$200 – $2,000 (Creative software, gear, or asset acquisition)",
            timeToFirstRevenue = "1 – 2 Months",
            scalabilityRating = "Very High (Assets can be licensed thousands of times simultaneously worldwide)",
            linkedModuleId = 5,
            linkedPrinciple = "Imagination",
            linkedModuleTitle = "Module 5: Imagination",
            linkedPrincipleRationale = "Intellectual property is pure crystallized imagination. Once created or acquired, it exists in the digital ether indefinitely, harvesting micropayments around the clock.",
            keySteps = listOf(
                "Identify high-demand commercial media needs (e.g., YouTube background audio, UI design vector kits, motion graphics presets).",
                "Produce a catalog of 20 to 50 premium, commercial-ready assets adhering to marketplace guidelines.",
                "Upload across major global licensing marketplaces with rigorous metadata and search tags.",
                "Reinvest early royalty streams into expanding the catalog or licensing third-party catalogs."
            ),
            prerequisites = listOf(
                "Creative production ability (audio, 3D, video, design) or capital to commission creators",
                "Understanding of digital licensing agreements and copyright rights",
                "SEO and metadata optimization skills for digital asset marketplaces"
            ),
            pros = listOf(
                "Zero fulfillment or inventory handling post-upload",
                "Global buyer base purchasing around the clock",
                "Long asset half-life: a high-quality audio track or 3D asset can generate royalties for a decade"
            ),
            considerations = listOf(
                "Revenue per individual license is small; requires a broad portfolio of assets to compound",
                "Platform marketplace fee commissions can be significant"
            ),
            notebookPrompt = "What creative digital asset could you produce once that businesses will need to license over and over?"
        ),
        IncomeIdea(
            id = "invest_private_credit",
            title = "Secured Private Lending & Peer Capital Pools",
            category = IncomeIdeaCategory.INVESTMENT_BASED,
            tagLine = "Lend capital against secured real estate collateral or cash-flowing receivables for fixed interest yields.",
            briefExplainer = "Act as the private bank. Provide short-term, asset-backed debt (such as bridge loans for real estate renovations or working capital secured by verified invoices). Returns come in the form of predictable monthly interest payments, strictly secured by underlying hard collateral.",
            effortLevel = EffortLevel.MEDIUM,
            capitalRequired = "$5,000 – $25,000+",
            timeToFirstRevenue = "1 Month (First interest payment cycle)",
            scalabilityRating = "High (Interest income can be rolled directly into new lending facilities)",
            linkedModuleId = 11,
            linkedPrinciple = "The Subconscious Mind",
            linkedModuleTitle = "Module 11: The Subconscious Mind",
            linkedPrincipleRationale = "The subconscious mind requires security, clarity, and unwavering confidence. Secured private lending operates on rigid contracts and collateral protection, removing emotional speculation from wealth accumulation.",
            keySteps = listOf(
                "Connect with established private lending syndicates or vetted peer-to-peer capital platforms.",
                "Establish strict underwriting boundaries: maximum 65-70% Loan-to-Value (LTV) on verified appraisals.",
                "Engage qualified legal counsel to draft first-lien promissory notes and recorded mortgages.",
                "Receive monthly automated interest distributions directly to your sovereign account."
            ),
            prerequisites = listOf(
                "Significant accumulated liquid capital seeking higher yields than bank deposits",
                "Understanding of debt covenants, lien positions, and collateral valuation",
                "Access to legal counsel and loan servicing infrastructure"
            ),
            pros = listOf(
                "Higher fixed annual yields (8-13% APR) compared to traditional fixed-income instruments",
                "First-lien collateral protection: in default, you can foreclose on the underlying hard asset",
                "Zero operational involvement in the borrower's day-to-day business"
            ),
            considerations = listOf(
                "Credit risk: borrower default can trigger lengthy foreclosure or restructuring proceedings",
                "Illiquid for the duration of the agreed loan term (typically 6-24 months)"
            ),
            notebookPrompt = "How can you transition from being a consumer of interest to becoming a sovereign provider of secured capital?"
        )
    )

    fun getIdeaById(id: String): IncomeIdea? {
        return ideas.firstOrNull { it.id == id }
    }

    fun getIdeasByCategory(category: IncomeIdeaCategory): List<IncomeIdea> {
        return ideas.filter { it.category == category }
    }

    fun getIdeasForModule(moduleId: Int): List<IncomeIdea> {
        return ideas.filter { it.linkedModuleId == moduleId }
    }
}
