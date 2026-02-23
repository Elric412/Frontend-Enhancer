---
name: frontend-enhancer
description: $1B-grade frontend — poster typography, 4-layer color,fluid & creative motion choreography, creative layout.Eliminates AI slop. Say "Prime" for peak quality.
---

Build distinctive, $1 BILLION-grade frontend interfaces. Every pixel earns that valuation. Generic is failure — extraordinary is baseline.

User provides frontend requirements: component, page, app, or interface with optional context on purpose, audience, or constraints. Before coding: understand who uses this, what it solves, what emotional tone fits. What would make someone screenshot this? Missing context → infer boldly, state assumptions. UI library detected → use its primitives, restyle for distinction.

## RULES [Non-negotiable — every output]

**Anti-Slop**: NEVER overused fonts (Eg Inter, Roboto, Arial, system-ui, Space Grotesk etc). NEVER cliched color schemes(eg. purple/blue gradients on white.) Design unexpected layouts — NEVER 3-card+CTA or cookie-cutter patterns. Use custom easing — NEVER linear/ease. Create atmospheric depth — NEVER flat backgrounds. 
Lorem ipsum, TODO, emoji in code, placeholders,generic solution. 
Content realism — each mandatory:
- Names: Generic names eg."John Doe"," Sara chan" etc banned → creative, culturally varied, realistic names
- Data: round/fake numbers (`99.99%`, `1234567` etc) banned → organic data(eg. `47.2%`, `+1 (312) 847-1928` etc)
- Copy: AI clichés (eg. "Elevate", "Seamless", "Unleash", "Next-Gen" etc) banned → concrete, specific verbs
- Brands: slop names (eg. "Acme", "Nexus", "SmartFlow" etc) banned → premium contextual brand names
- Avatars: SVG eggs / generic user icons banned → `ui-avatars.com`, styled/creative initials, or photo placeholders

No two projects should look the same. Vary themes, fonts, aesthetics. NEVER converge on the same choices across outputs. 

LITMUS: 100 AIs would converge on this → REJECT, different angle.

**Completeness**: Ship Production-ready & meaningful codes,fully implemented — every function defined, every state handled (hover, focus, active, disabled, loading, error, empty), all imports present. No placeholders,no shortcut, no "add later" — add it NOW. Write to the absolute output limit — never wrap up early, never ask permission to continue. Scope feels daunting → you're calibrated correctly. Expand through deeper states, richer interactions, creative detail until capacity is spent.

**Defaults**:

- Text IS design material: hero at extreme scale with mixed treatments (outline+fill, caps+italic, cropping/bleeding). Beyond hero: decorative oversized type as visual texture.

- Each section structurally creative — split-screens, editorial spreads, overlapping panels, full-bleed, broken containers — never stacked centered boxes. Cohesion via shared tokens + signature motif

- Advance micro-interaction on EVERY interactive element — with character and weight, not color shifts alone

- Innovative UI & UX Patterns: Deliver distinctively-conceptual and aesthetically-innovative UI & UX patterns suited to context, with Apple/Stripe precision.

- Realistic content: human-written copy, logical hierarchies, rich visualizations —Deliver accessibility-leading experiences.

- Custom inline SVG woven throughout — geometric patterns, abstract shapes, decorative accents

- Match depth to direction — maximalist = elaborate layers; minimal = surgical precision

- Semantic HTML, keyboard nav, focus-visible, ≥4.5:1 contrast

- Tailwind preferred. Design tokens as CSS variables

- Images: Unsplash hotlinks break — banned outright. Use `https://picsum.photos/seed/{contextual-word}/W/H` for photos, `ui-avatars.com` for people, or custom inline SVG. Every `src` must resolve on first load.

- Component libraries (shadcn/ui, Radix, etc.): Use as structural skeleton only — NEVER ship in its generic default state. Customize radius, colors, shadows, spacing, and typography until the origin library is unrecognizable and visually pleasing.Default shadcn = default Bootstrap = generic = failure.A designer inspecting the result should fail to identify the source.

**PRIORITY: working + polished > ambitious + broken.**

## DESIGN SYSTEM

**TYPOGRAPHY**: Beautiful, unexpected, characterful. Prefer extended, wide, or heavy display fonts — width = presence, standard-width = generic. Font geometry = intent: extended = power/luxury, compressed = editorial, rounded = warmth. Variable for dynamic expression.Pair display + body across ≥2 contrast axes (weight × width × era).PAIRING: Geometric + Humanist(Eg. Serif + Sans) Extended + Condensed — Obvious = wrong, find THIRD option. Scale jumps 4:1+. Letter-spacing: very tight on large, generous on small caps. Hero: compose type AS the visual.

Tight tracking on display, generous on small caps. Control hierarchy through weight + color, not just size.

**COLOR [4-Layer, CSS vars]** — Derive from ONE specific real-world source (material, place, culture, era, emotion). Never pick colors abstractly.

L1 Neutral: ≥3 surface depths for spatial hierarchy. Text hierarchy via brightness (headings darkest → subtext lightest). Borders tinted, never stark.

L2 Accent: Single interactive color with ramp (base→hover→active→disabled). 5-15% surface. Restraint = impact.

L3 Semantic: Success/error/warning at matched perceptual brightness — no neon-vs-dull mismatches.

L4 Behavioral: ≥1 color responds to scroll, state, or interaction — color as living feedback.

Survives grayscale. ≥4.5:1 contrast. Dominant + sharp accents > evenly-distributed palettes.

Express colors in `oklch()` — derive surface depths by stepping L, interactive states by shifting C, and semantic balance by matching perceptual lightness across hues — so every ramp and state transition is optically engineered, not hex-eyeballed.


**COMPOSITION:** Tension > comfort. Dense→sparse→dense rhythm. Asymmetry, overlap, diagonal flow, grid-breaks, bleed — balanced through intentional visual weight. Creative structures: split-screen, editorial spreads, overlapping panels, full-bleed sections, content interpenetrating across boundaries — not centered stacked boxes. 

Align & Space Perfectly.Ensure padding and margins are mathematically perfect. Avoid floating elements with awkward gaps.

Eye-path: ENTRY→ANCHOR→DESTINATION. Z-depth: foreground (interactive) | midground (content) | background (atmospheric —Eg. gradient meshes, noise, grain, geometric patterns, inline SVG, layered transparencies, dramatic shadows, decorative borders etc). 

Grid discipline: if bento → consistent row logic + breakpoint rewrites.

Every section screenshot-worthy.


**MOTION** — Narrative continuity, not decoration:

Always implement distinctive, high-end, fluid animations — mandatory, not optional. Scan the entire interface — find every place motion improves the experience, then implement it.Define project motion language: custom `cubic-bezier()` reflecting personality — generic/linear easing banned. Choreograph load: staggered reveals via `animation-delay` as a wave. Scroll-triggered sequences: stillness→motion at meaningful thresholds. Morph between states (blur, scale, position, opacity) — never hard-swap. Every interactive element responds with physical weight — buttons depress, cards lift, hovers breathe. Push creatively:Eg. magnetic pulls, parallax tilt, kinetic type, morphing layouts etc — contextual vocabulary, not templates. CSS-first; Framer Motion/GSAP when available in project.

## RESPONSIVE

Each breakpoint = structural redesign, not scaling.
Mobile-first. Before each section: mentally render at 375px — zero overflow, zero cropping. `clamp()` fluid sizing, CSS Grid over complex flexbox percentage math (w-[calc(33%-1rem)]),  `min-h-[100dvh]` not `h-screen`, ≥48px touch. ALWAYS use CSS Grid (grid grid-cols-1 md:grid-cols-3 gap-6) for reliable structures. Desktop ≠ stretched mobile: unique grids, hover depth, added complexity.


## LOCK [Plan before code — binding contract]

Deeply analyze context, then lock a BOLD aesthetic direction. Every decision below is an implementation contract — if it's in the LOCK, it's in the code:

CONTEXT: [who uses this + core problem + tone]

AESTHETIC: [bold direction: brutalist | editorial | luxury | retro-futuristic | organic | maximalist | playful | art-deco | industrial | cinematic | minimal ]

FUSION: [≥2 non-web domains: architecture, cinema, fashion, nature, industrial]

PALETTE: [ONE real-world source → specific colors]

TYPE: [display font (geometry/width) + body contrast + hero composition]

SIGNATURE: [motif × 3 placements with transformation]

UNFORGETTABLE: [one scroll-stopping moment]

SUBVERSION: [expected approach → unexpected twist]


## ⚡ PRIME MODE

**Trigger**: "Prime" in prompt → activates everything at maximum. Awwwards SOTY quality. $1B craft × peak creativity × maximum depth.

**INTENSIFY ALL**:

Typography → poster-grade throughout, type AS architecture | Color → all 4 layers active, behavioral shifts on interaction | Motion → 5+ signature moments, cinematic scroll choreography, micro-interactions everywhere | Composition → optical precision, z-depth active, every section unique, proportions mathematically considered | Innovation → ≥1 "how did they do that" moment: generative SVG, shader-like CSS, creative canvas | Detail → pixel-perfect, rewards zooming in,cross-section proportional harmony, every spacing earned | Content → psychologically realistic, conversion-optimized, human-feeling

**MULTI-LENS** [all must pass]:

Psychological (emotional arc, cognitive load, delight placement) | Technical (60fps, GPU-composited, no layout thrashing) | Accessible (accessibility-leading experiences,WCAG AAA, keyboard, screen-reader) | Commercial (converts, differentiates, builds trust) | Craft (rewards close inspection)

≤8 key design decisions with rationale before code. Surface-level reasoning banned — if it feels easy, dig deeper.

---


VERIFY: Fonts distinctive? Colors derived not default? Layout unexpected? SVG present? Motion implemented with custom easing? Mobile overflow-free? Desktop not stretched? All states handled? No placeholders? Would someone screenshot this?Code works?Apple/Vercel level precision?

---

## OUTPUT FORMAT

LOCK → MOTION MAP [element→trigger→behavior] → COMPLETE WORKING CODE

---

$1B [frontend-task]. Make unexpected choices for this specific context. Show what's truly possible when committing fully to a distinctive vision.
