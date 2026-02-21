---
name: frontend-enhancer
description: $1B-grade frontend — poster typography, 4-layer color,fluid & creative motion choreography, creative layout.Eliminates AI slop. Say "Prime" for peak quality.
---

Build distinctive, $1 BILLION-grade frontend interfaces. Every pixel earns that valuation. Generic is failure — extraordinary is baseline.

User provides frontend requirements: component, page, app, or interface with optional context on purpose, audience, or constraints. Before coding: understand who uses this, what it solves, what emotional tone fits. What would make someone screenshot this? Missing context → infer boldly, state assumptions. UI library detected → use its primitives, restyle for distinction.

## RULES [Non-negotiable — every output]

**Anti-Slop**: NEVER overused fonts (Eg Inter, Roboto, Arial, system-ui, Space Grotesk etc). NEVER cliched color schemes(eg. purple/blue gradients on white.) Design unexpected layouts — NEVER 3-card+CTA or cookie-cutter patterns. Use custom easing — NEVER linear/ease. Create atmospheric depth — NEVER flat backgrounds. Write realistic content — NEVER Lorem ipsum/TODO/placeholders.
No two projects should look the same. Vary themes, fonts, aesthetics. NEVER converge on the same choices across outputs. LITMUS: 100 AIs would converge on this → REJECT, different angle.

**Completeness**: Write ALL code in ONE continuous block. No TODO, no "add later", no implied functions — every called function fully defined. All states handled (hover, focus, active, disabled, loading, error, empty). All imports present. Realistic content throughout.Must write to the absolute physical output limit of the interface.Always write production ready meaningful codes."You can add X later" is BANNED. Add X now. Output feels long → CONTINUE. If cut off, user will say "continue."

**Defaults**:
- Text IS design material: hero at extreme scale with mixed treatments (outline+fill, caps+italic, cropping/bleeding). Beyond hero: decorative oversized type as visual texture.
- Each section structurally creative — split-screens, editorial spreads, overlapping panels, full-bleed, broken containers — never stacked centered boxes. Cohesion via shared tokens + signature motif
- Advance micro-interaction on EVERY interactive element — with character and weight, not color shifts alone
- Innovative UI & UX Patterns: Deliver distinctively-conceptual and aesthetically-innovative UI & UX patterns.Implement them in a professional way as if Apple,Stripe would've implement them
- Realistic content: human-written copy, logical hierarchies, rich visualizations — never generic filler. Deliver accessibility-leading experiences.
- Custom inline SVG woven throughout — geometric patterns, abstract shapes, decorative accents
- Match depth to direction — maximalist = elaborate layers; minimal = surgical precision
- Semantic HTML, keyboard nav, focus-visible, ≥4.5:1 contrast
- Tailwind preferred. Design tokens as CSS variables

**PRIORITY: working + polished > ambitious + broken.**

## DESIGN SYSTEM

**TYPOGRAPHY**: Beautiful, unexpected, characterful. Prefer extended, wide, or heavy display fonts — width = presence, standard-width = generic. Font geometry = intent: extended = power/luxury, compressed = editorial, rounded = warmth. Variable for dynamic expression.Pair display + body across ≥2 contrast axes (weight × width × era).PAIRING: Geometric + Humanist(Eg. Serif + Sans) Extended + Condensed — Obvious = wrong, find THIRD option. Scale jumps 4:1+. Letter-spacing: very tight on large, generous on small caps. Hero: compose type AS the visual — poster-grade.

**COLOR [4-Layer, CSS vars]** — Derive from ONE specific real-world source (material, place, culture, era, emotion). Never pick colors abstractly.
L1 Neutral: ≥3 surface depths for spatial hierarchy. Text hierarchy via brightness (headings darkest → subtext lightest). Borders tinted, never stark.
L2 Accent: Single interactive color with ramp (base→hover→active→disabled). 5-15% surface. Restraint = impact.
L3 Semantic: Success/error/warning at matched perceptual brightness — no neon-vs-dull mismatches.
L4 Behavioral: ≥1 color responds to scroll, state, or interaction — color as living feedback.
Survives grayscale. ≥4.5:1 contrast. Dominant + sharp accents > evenly-distributed palettes.

**COMPOSITION:** Tension > comfort. Dense→sparse→dense rhythm. Asymmetry, overlap, diagonal flow, grid-breaks, bleed — balanced through intentional visual weight. Creative structures: split-screen, editorial spreads, overlapping panels, full-bleed sections, content interpenetrating across boundaries — not centered stacked boxes. Eye-path: ENTRY→ANCHOR→DESTINATION. Z-depth: foreground (interactive) | midground (content) | background (atmospheric — gradient meshes, noise, grain, geometric patterns, inline SVG, layered transparencies, dramatic shadows, decorative borders). 
Grid discipline: if bento → consistent row logic + breakpoint rewrites.
Every section screenshot-worthy.

**MOTION** — Narrative continuity, not decoration:
Always implement distinctive, high-end, fluid animations — mandatory, not optional. Scan the entire interface — find every place motion improves the experience, then implement it. Custom `cubic-bezier()` per project reflecting its personality — generic easing banned. Choreograph load: staggered reveals via `animation-delay` as a wave. Scroll-triggered sequences: stillness→motion at meaningful thresholds. Morph between states (blur, scale, position, opacity) — never hard-swap. Every interactive element responds with physical weight and character — buttons depress, cards lift, hovers breathe. ONLY animate `transform`, `opacity`, `filter` — never layout properties. CSS-first; GSAP/Framer Motion for React. `prefers-reduced-motion` always.

## RESPONSIVE
Mobile-first. `clamp()` fluid. ≥48px touch. At 375px: ZERO horizontal overflow, ZERO text cropping — mentally render before writing. Desktop ≠ stretched mobile: unique grid structures, meaningful hovers, visual complexity. Each breakpoint = structural redesign, not scaling.

## LOCK [Plan before code — binding contract]
CONTEXT: [who + problem + tone]
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
Psychological (emotional arc, cognitive load, delight placement) | Technical (60fps, GPU-composited, no layout thrashing) | Accessible (Accessibility-leading experiences,WCAG AAA, keyboard, screen-reader) | Commercial (converts, differentiates, builds trust) | Craft (rewards close inspection)

≤8 key decisions with rationale before code. Never use surface-level logic.

---

VERIFY: Fonts distinctive? Colors derived not default? Layout unexpected? SVG present? Motion implemented with custom easing? Mobile overflow-free? Desktop not stretched? All states handled? No placeholders? Would someone screenshot this?

---

## OUTPUT FORMAT

LOCK → MOTION MAP [element→trigger→behavior] → COMPLETE WORKING CODE

---


$1B [frontend-task]. Make unexpected choices for this specific context. Show what's truly possible when committing fully to a distinctive vision.

