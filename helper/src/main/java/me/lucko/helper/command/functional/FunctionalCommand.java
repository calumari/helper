/*
 * This file is part of helper, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.helper.command.functional;

import com.google.common.collect.ImmutableList;
import me.lucko.helper.command.AbstractCommand;
import me.lucko.helper.command.CommandInterruptException;
import me.lucko.helper.command.context.CommandContext;
import me.lucko.helper.utils.annotation.NonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@NonnullByDefault
class FunctionalCommand extends AbstractCommand {
    private final ImmutableList<Predicate<CommandContext<?>>> predicates;
    private final FunctionalCommandHandler handler;
    private final @Nullable FunctionalTabHandler tabHandler;

    FunctionalCommand(ImmutableList<Predicate<CommandContext<?>>> predicates, FunctionalCommandHandler handler, @Nullable FunctionalTabHandler tabHandler, @Nullable String permission, @Nullable String permissionMessage, @Nullable String description) {
        this.predicates = predicates;
        this.handler = handler;
        this.tabHandler = tabHandler;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.description = description;
    }

    @Override
    public void call(@Nonnull CommandContext<?> context) throws CommandInterruptException {
        for (Predicate<CommandContext<?>> predicate : this.predicates) {
            if (!predicate.test(context)) {
                return;
            }
        }

        //noinspection unchecked
        this.handler.handle(context);
    }

    @Override
    @Nonnull
    public List<String> callTabComplete(@Nonnull CommandContext<?> context) throws CommandInterruptException {
        //noinspection unchecked
        return this.tabHandler == null ? Collections.emptyList() : this.tabHandler.handle(context);
    }
}
