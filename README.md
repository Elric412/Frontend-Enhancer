# Frontend Enhancer ⚡

**A skill that makes AI build frontends that don't look like AI built them.**

---

## The Problem

Every AI builds the same frontend. Same fonts (Inter, Roboto). Same colors (purple-blue gradient). Same layout (hero → 3 cards → CTA → footer). Same hover effects (color shift). Same flat backgrounds.

The community calls it **AI slop**. It happens because LLMs converge on the most probable output from their training data — and the most probable frontend is a generic SaaS landing page.

## What This Does

Frontend Architect is a skill that breaks that convergence. It installs a different design system into the model's reasoning:

- **Text as design material** — Oversized, cropped, overlapping, outlined, decorative. Not just readable content but visual architecture. Poster-grade hero typography with mixed treatments (outline + fill, caps + italic, cropping, bleeding).
- **Creative layouts** — Split-screens, editorial spreads, overlapping panels, full-bleed sections, broken containers. Not stacked centered boxes. Every section structurally unique.
- **4-layer color** — Neutrals with spatial hierarchy, accent ramps (not single hex), semantic colors at matched perceptual brightness, and behavioral colors that respond to scroll/state. All derived from one real-world source.
- **Motion choreography** — Staggered load sequences, scroll-triggered reveals, state morphing with physical weight. Bound to a motion map so it actually gets implemented. Every interactive element responds with character.
- **Custom SVG** — Geometric patterns, abstract shapes, decorative accents woven throughout as visual texture.
- **Domain fusion** — Every design fuses 2+ non-web domains (architecture, cinema, fashion, editorial, nature) making generic output structurally impossible.
- **Anti-slop litmus** — "Would 100 AIs converge on this? Yes → reject, different angle."

It also includes a completeness system that prevents the model from wrapping up early or leaving TODOs, and a planning phase (LOCK) that forces the model to commit to a specific aesthetic direction before writing any code.

## Quick Start

1. Ask for any frontend task
2. Say **"Prime"** in your prompt for maximum quality

That's it. No setup, no dependencies, no config.

### Works With
- **Claude** 
- **ChatGPT** 
- **Gemini** 
- **Cursor** 
- **Windsurf / Cline / Aider** 
- Any tool that accepts system-level instructions

## Usage

### Default Mode
```
Build a landing page for a creative agency that specializes in brand identity.
```

### Prime Mode (Recommended)
```
Prime Mode: Build a landing page for a creative agency that specializes in brand identity.
```

I created Prime Mode because LLMs tend to take the path of least resistance,they'll pick the first decent font, the first reasonable color, the first layout that works, and call it done. Prime forces the model to slow down and actually design.

When Prime is active, the model analyzes every decision through multiple lenses. It outputs up to 8 key design decisions with rationale before writing any code, so it's committing to intentional choices instead of defaulting to whatever comes first.

It also tries to crank every dimension to its peak

## What's Inside

| Section | What It Does |
|---------|-------------|
| **Anti-Slop** | Bans generic fonts, cliched colors, and cookie-cutter layouts. Anti-convergence across outputs. 100-AI litmus test. |
| **Completeness** | Every function fully defined, all states handled, no TODOs, write to output limit. "Add later" is banned — add it now. |
| **Defaults** | Rules enforced on every output: text-as-graphic, creative layouts, micro-interactions, innovative UI/UX patterns, SVG, realistic content, and accessibility. |
| **Typography** | Extended/wide display fonts, intentional font geometry, smart pairing across contrast axes (Geometric + Humanist, Extended + Condensed), 4:1+ scale jumps, poster-grade hero composition. |
| **Color** | 4-layer system — L1 Neutral (spatial hierarchy via surface depths), L2 Accent (interactive ramp), L3 Semantic (perceptually matched), L4 Behavioral (responds to scroll/state). Derived from one real-world source. |
| **Composition** | Tension-based layouts, z-depth planes, creative structures (split-screen, editorial, overlapping panels), balanced visual weight. Bento grid discipline when applicable. |
| **Motion** | Scan the entire interface for motion opportunities, then implement all of them. Custom cubic-bezier per project, staggered load choreography, scroll-triggered sequences, state morphing. Physical weight on every interaction. |
| **LOCK** | Mandatory planning: context, aesthetic direction, domain fusion, palette from real-world source, typography strategy, signature motif, unforgettable moment, and subversion of expectations. |
| **Prime** | Maximum quality trigger: intensifies every dimension, multi-lens analysis (psychological, technical, accessible, commercial, craft), key decisions with rationale. |

## How It Was Built

130+ iterations of the V1 skill using role reversal method. Then a month of V2 development including:

- Real-world comparisons against Anthropic's official frontend skill.
- Architectural reviews from multiple AI models
- User testing and feedback from the community
- Integrated some of the community's tricks and Claude frontend aesthetic into this

## Tips

- **Always use Prime** for important projects. It's the difference between "good" and "screenshot-worthy."
- **Give context.** "Landing page for a cybersecurity startup targeting enterprise CTOs" produces way better results than "landing page for a startup."
- **Let it plan.** The LOCK output isn't wasted tokens — it's what makes the design coherent.
- **Trust the fusion.** "Architecture + cinema" or "fashion + industrial" sounds weird but produces genuinely original designs.

## Contributing

Found an issue? Have a suggestion? Open an issue or PR.

Also u can add your own rules in the default section.

---

**Built for the community. If it makes your frontends better, star the repo ⭐**
